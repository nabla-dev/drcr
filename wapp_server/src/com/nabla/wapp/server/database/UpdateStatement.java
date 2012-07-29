/**
* Copyright 2012 nabla
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*
*/
package com.nabla.wapp.server.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.ArgumentList;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateStatement<T> extends SqlStatement {

	private static final Log	log = LogFactory.getLog(UpdateStatement.class);

	private final String						sqlTemplate;
	private final List<IStatementParameter>	parameters = new ArrayList<IStatementParameter>();
	private IStatementParameter					recordId = null;
	private String								uniqueFieldName = null;	// null if n/a

	public UpdateStatement(final Class<T> recordClass) {
		final IRecordTable table = commonConstructor(recordClass);
		Assert.state(recordId != null);
		Assert.notNull(table);
		// build SQL template
		sqlTemplate = "UPDATE " + table.name() + " SET {0} WHERE " + recordId.getName() + "=?;";
	}

	public UpdateStatement(final Class<T> recordClass, final String sqlTemplate) {
		commonConstructor(recordClass);
		Assert.state(recordId != null);
		this.sqlTemplate = sqlTemplate;
	}

	@SuppressWarnings("unchecked")
	private IRecordTable commonConstructor(final Class clazz) {
		if (clazz == null)
			return null;
		final IRecordTable t = (IRecordTable) clazz.getAnnotation(IRecordTable.class);
		for (Field field : clazz.getDeclaredFields()) {
			final IRecordField definition = field.getAnnotation(IRecordField.class);
			if (definition == null)
				continue;
			if (definition.id())
				recordId = createParameter(field);
			else {
				if (definition.unique())
					uniqueFieldName = field.getName();
				parameters.add(createParameter(field));
			}
		}
		final IRecordTable tt = commonConstructor(clazz.getSuperclass());
		return (t == null) ? tt : t;
	}

	public void execute(final Connection conn, final T record) throws SQLException, DispatchException, ValidationException {
		Assert.argumentNotNull(conn);

		final List<IStatementParameter> parametersToUpdate = new ArrayList<IStatementParameter>();
		final ArgumentList updates = new ArgumentList();
		for (IStatementParameter parameter : parameters) {
			if (!parameter.include(record))
				continue;
			parametersToUpdate.add(parameter);
			updates.add(parameter.getName() + "=?");
		}
		if (parametersToUpdate.isEmpty()) {
			if (log.isDebugEnabled())
				log.debug("no values to update");
			return;
		}
		final String sql = MessageFormat.format(sqlTemplate, updates.toString());
		if (log.isDebugEnabled())
			log.debug("SQL=" + sql);
		final PreparedStatement stmt = conn.prepareStatement(sql);
		try {
			int i = 1;
			for (IStatementParameter parameter : parametersToUpdate)
				parameter.write(stmt, i++, record);
			recordId.write(stmt, i++, record);
			if (stmt.executeUpdate() != 1)
				throw new DispatchException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
		} catch (final SQLException e) {
			if (uniqueFieldName != null && SQLState.valueOf(e) == SQLState.INTEGRITY_CONSTRAINT_VIOLATION) {
				if (log.isErrorEnabled())
					log.error("SQL error " + e.getErrorCode() + "-"	+ e.getSQLState(), e);
				throw new ValidationException(uniqueFieldName, CommonServerErrors.DUPLICATE_ENTRY);
			}
			throw e;
		} finally {
			Database.close(stmt);
		}
	}

}
