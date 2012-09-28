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
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.general.ArgumentList;

/**
 * @author nabla
 *
 */
public class SqlInsert<T> {

	private static final Log					log = LogFactory.getLog(SqlInsert.class);
	private final String						sql;
	private final List<IStatementParameter>	parameters = new ArrayList<IStatementParameter>();
	private final boolean						autoGeneratedKeys;

	public SqlInsert(final Class<T> recordClass, boolean autoGeneratedKeys, final SqlInsertOptions option) {
		this(recordClass, createSqlTemplate(getTable(recordClass).name(), option), autoGeneratedKeys);
	}

	public SqlInsert(final Class<T> recordClass, final SqlInsertOptions option) {
		this(recordClass, false, option);
	}

	public SqlInsert(final Class<T> recordClass, boolean autoGeneratedKeys) {
		this(recordClass, autoGeneratedKeys, SqlInsertOptions.INSERT);
	}

	public SqlInsert(final Class<T> recordClass) {
		this(recordClass, SqlInsertOptions.INSERT);
	}

	public SqlInsert(final Class<T> recordClass, final String sqlTemplate) {
		this(recordClass, sqlTemplate, false);
	}

	public SqlInsert(final Class<T> recordClass, final String sqlTemplate, boolean autoGeneratedKeys) {
		Assert.argumentNotNull(sqlTemplate);

		this.autoGeneratedKeys = autoGeneratedKeys;
		buildParameterList(recordClass);
		final ArgumentList names = new ArgumentList();
		final ArgumentList values = new ArgumentList();
		final ArgumentList updates = new ArgumentList();
		for (IStatementParameter parameter : parameters) {
			names.add(parameter.getName());
			values.add("?");
			if (!parameter.isUnique())
				updates.add(parameter.getName() + "=VALUES(" + parameter.getName() + ")");
		}
		sql = MessageFormat.format(sqlTemplate, names.toString(), values.toString(), updates.toString());
		if (log.isDebugEnabled())
			log.debug("SQL=" + sql);
	}

	public static String createSqlTemplate(final String tableName, final SqlInsertOptions option) {
		switch (option) {
		case APPEND:
			return "INSERT IGNORE INTO " + tableName + " ({0}) VALUES({1});";
		case OVERWRITE:
			return "INSERT INTO " + tableName + " ({0}) VALUES({1}) ON DUPLICATE KEY UPDATE {2};";
		case REPLACE:
			return "REPLACE INTO " + tableName + " ({0}) VALUES({1});";
		case INSERT:
		default:
			return "INSERT INTO " + tableName + " ({0}) VALUES({1});";
		}
	}

	public PreparedStatement prepareStatement(final Connection conn) throws SQLException {
		Assert.argumentNotNull(conn);

		return conn.prepareStatement(sql, autoGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
	}

	public BatchInsertStatement<T> prepareBatchStatement(final Connection conn) throws SQLException {
		return new BatchInsertStatement<T>(conn, this);
	}

	public List<IStatementParameter> getParameters() {
		return parameters;
	}

	public boolean getAutoGenerateKeys() {
		return autoGeneratedKeys;
	}

	@SuppressWarnings("unchecked")
	protected static IRecordTable getTable(final Class clazz) {
		Assert.notNull(clazz, "have you forgotten to set @IRecordTable for record " + clazz.getSimpleName());

		final IRecordTable t = (IRecordTable) clazz.getAnnotation(IRecordTable.class);
		return (t != null) ? t : getTable(clazz.getSuperclass());
	}

	protected void buildParameterList(final Class clazz) {
		if (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				final IRecordField definition = field.getAnnotation(IRecordField.class);
				if (definition != null)
					parameters.add(SqlStatement.createParameter(field));
			}
			buildParameterList(clazz.getSuperclass());
		}
	}

}
