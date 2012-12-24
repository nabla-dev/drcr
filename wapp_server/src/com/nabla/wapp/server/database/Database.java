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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Assert;

/**
 * @author nabla
 *
 */
public class Database implements IDatabase {

	public static final String		PRODUCTION_MODE = "production_mode";
	private static final Log			log = LogFactory.getLog(Database.class);
	private final IConnectionPool		pool;

	/**
	 * Constructor
	 * @param dbName			- database name as defined in pool
	 * @param serverContext		- web app context
	 * @throws SQLException
	 */
	public Database(final String dbName, final ServletContext serverContext) throws SQLException {
		Assert.argumentNotNull(serverContext);

		pool = ("1".equals(serverContext.getInitParameter(PRODUCTION_MODE))) ?
				new TomcatConnectionPool(dbName)
			:
				new CommonConnectionPool(dbName, serverContext)	;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return pool.get();
	}

	public static void close(final Connection conn) {
		try { conn.close(); } catch (final SQLException e) {}
	}

	public static void close(final Statement stmt) {
		try { stmt.close(); } catch (final SQLException e) {}
	}

	public static void close(final ResultSet rs) {
		try { rs.close(); } catch (final SQLException e) {}
	}

	public static boolean isBatchCompleted(final int[] results) {
		if (results == null)
			return false;
		for (int i = 0; i < results.length; i++) {
			final int result = results[i];
			if (result < 0 && result != Statement.SUCCESS_NO_INFO)
				return false;
		}
		return true;
	}

	public static boolean executeUpdate(final Connection conn, final String sql, final Object... parameters) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, sql, parameters);
		try {
			return stmt.executeUpdate() == 1;
		} finally {
			stmt.close();
		}
	}

	public static Integer addRecord(final Connection conn, final String sql, final Object... parameters) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, Statement.RETURN_GENERATED_KEYS, sql, parameters);
		try {
			if (stmt.executeUpdate() != 1) {
				if (log.isErrorEnabled())
					log.error("failed to add record");
				return null;
			}
			final ResultSet rsKey = stmt.getGeneratedKeys();
			try {
				rsKey.next();
				return rsKey.getInt(1);
			} finally {
				rsKey.close();
			}
		} finally {
			stmt.close();
		}
	}

	public static Integer addUniqueRecord(final Connection conn, final String sql, final Object... parameters) throws SQLException {
		try {
			return addRecord(conn, sql, parameters);
		} catch (final SQLException e) {
			if (SQLState.valueOf(e) == SQLState.INTEGRITY_CONSTRAINT_VIOLATION)
				return null;
			throw e;
		}
	}

	/**
	 * Get java <b>Integer</b> value from ResultSet (and not <b>int</b> value as it is the default!)
	 * @param rs - query result
	 * @param column - name of column
	 * @return null if it was NULL, value otherwise
	 * @throws SQLException
	 */
	public static Integer getInteger(final ResultSet rs, final String column) throws SQLException {
		Assert.argumentNotNull(rs);

		int value = rs.getInt(column);
		return rs.wasNull() ? null : value;
	}

	/**
	 * Test if a table has any data
	 * @param conn		- database connection
	 * @param tableName	- table name
	 * @return success
	 * @throws SQLException
	 */
	public static boolean isTableEmpty(final Connection conn, final String tableName) throws SQLException {
		final Statement stmt = conn.createStatement();
		try {
			final ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + ";");
			try {
				return rs.next() && rs.getInt(1) == 0;
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

}
