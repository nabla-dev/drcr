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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.ArgumentList;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class InsertStatement<T> extends SqlStatement {

	private static final Log	log = LogFactory.getLog(InsertStatement.class);

	private final String						sqlTemplate;
	private final List<IStatementParameter>	parameters = new ArrayList<IStatementParameter>();
	private String								uniqueFieldName = null;	// null if n/a

	public InsertStatement(final Class<T> recordClass) {
		final IRecordTable table = commonConstructor(recordClass);
		Assert.notNull(table, "have you defined @IRecordTable for your class?");
		// build SQL template
		sqlTemplate = "INSERT INTO " + table.name() + " ({0}) VALUES({1});";
	}

	public InsertStatement(final Class<T> recordClass, final String sqlTemplate) {
		commonConstructor(recordClass);
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
			if (definition.unique())
				uniqueFieldName = field.getName();
			parameters.add(createParameter(field));
		}
		final IRecordTable tt = commonConstructor(clazz.getSuperclass());
		return (t == null) ? tt : t;
	}

	public int execute(final Connection conn, final T record) throws SQLException, ValidationException, InternalErrorException {
		Assert.argumentNotNull(conn);

		final List<IStatementParameter> parametersToInsert = new ArrayList<IStatementParameter>();
		final ArgumentList names = new ArgumentList();
		final ArgumentList values = new ArgumentList();
		for (IStatementParameter parameter : parameters) {
			if (!parameter.include(record))
				continue;
			parametersToInsert.add(parameter);
			names.add(parameter.getName());
			values.add("?");
		}
		if (parametersToInsert.isEmpty()) {
			if (log.isErrorEnabled())
				log.error("no values to insert!!!!");
			Util.throwInternalErrorException("no parameter values given for SQL statement");
		}
		final String sql = MessageFormat.format(sqlTemplate, names.toString(), values.toString());
		if (log.isDebugEnabled())
			log.debug("SQL=" + sql);
		final PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		try {
			int i = 1;
			for (IStatementParameter parameter : parametersToInsert)
				parameter.write(stmt, i++, record);
			if (stmt.executeUpdate() != 1) {
				if (log.isErrorEnabled())
					log.error("failed to add record");
				Util.throwInternalErrorException("failed to execute SQL statement");
			}
			final ResultSet rsKey = stmt.getGeneratedKeys();
			try {
				rsKey.next();
				return rsKey.getInt(1);
			} finally {
				Database.close(rsKey);
			}
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
