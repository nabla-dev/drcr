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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.xml.XmlDate;
import com.nabla.wapp.server.xml.XmlInteger;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.general.StringSet;

/**
 * @author nabla
 *
 */
public class StatementFormat {

	private static final Log							log = LogFactory.getLog(StatementFormat.class);
	private static final Map<Class, IStatementSetter>	cache = new HashMap<Class, IStatementSetter>();
	private static final DefaultSetter				defaultSetter = new DefaultSetter();

	static {
		cache.put(String.class, new VarcharSetter());
		cache.put(Integer.class, new IntegerSetter());
		cache.put(int.class, new IntegerSetter());	// Field class wraps primitives!!!
		cache.put(Boolean.class, new BooleanSetter());
		cache.put(boolean.class, new BooleanSetter());	// Field class wraps primitives!!!
		cache.put(Date.class, new DateSetter());
		cache.put(java.util.Date.class, new UtilDateSetter());
		cache.put(IntegerSet.class, new IntegerSetSetter());
		cache.put(StringSet.class, new StringSetSetter());
		cache.put(InputStream.class, new InputStreamSetter());
		cache.put(XmlString.class, new XmlStringSetter());
		cache.put(XmlInteger.class, new XmlIntegerSetter());
		cache.put(XmlDate.class, new XmlDateSetter());
	}

	public static IStatementSetter getSetter(final Class parameterClass) {
		Class c = parameterClass;
		while (c != null) {
			final IStatementSetter writer = cache.get(c);
			if (writer != null)
				return writer;
			c = c.getSuperclass();
		}
		if (log.isDebugEnabled())
			log.debug("found no SQL statement setter for type '" + parameterClass.getSimpleName() + "'");
		return defaultSetter;
	}

	public static PreparedStatement format(final PreparedStatement stmt, Object... parameters) throws SQLException {
		if (parameters != null) {
			int i = 1;
			for (Object parameter : parameters) {
				Assert.notNull(parameter, "you cannot give a null value as argument to PreparedStatement");
				i += getSetter(parameter.getClass()).setValue(stmt, i, parameter);
			}
		}
		return stmt;
	}

	public static PreparedStatement prepare(final Connection conn, final String sql, Object... parameters) throws SQLException {
		return prepare(conn, Statement.NO_GENERATED_KEYS, sql, parameters);
	}

	public static PreparedStatement prepare(final Connection conn, int autoGeneratedKeys, final String sql, Object... parameters) throws SQLException {
		Assert.argumentNotNull(conn);

		if (parameters == null)
			return conn.prepareStatement(sql, autoGeneratedKeys);
		StringBuilder actualSql = new StringBuilder(sql);
		int i = 1;
		for (Object parameter : parameters) {
			Assert.notNull(parameter);
			i += getSetter(parameter.getClass()).prepare(actualSql, i, parameter);
		}
		final PreparedStatement stmt = conn.prepareStatement(actualSql.toString(), autoGeneratedKeys);
		try {
			return format(stmt, parameters);
		} catch (SQLException e) {
			Database.close(stmt);
			throw e;
		}
	}

}
