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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.nabla.wapp.server.general.Assert;


/**
 * @author nabla
 *
 */
public class Database implements IDatabase {

	private static final Log				log = LogFactory.getLog(Database.class);
	private static final PoolingDriver	driver = new PoolingDriver();

	private final String	url;

	/**
	 * Constructor
	 * @param name - database name as defined in pool
	 * @throws ClassNotFoundException 
	 */
	public Database(final String dbName, final String driverName) throws ClassNotFoundException {
		url = "jdbc:apache:commons:dbcp:/" + dbName;
		if (!isLoaded(dbName)) {
			load(dbName);
			Class.forName(driverName);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}

	/**
	 * Load database pool
	 * @param name - database name as defined in pool
	 */
	public static void load(final String name) {
		if (log.isDebugEnabled())
			log.debug("loading pool for database '" + name + "'");
		driver.registerPool(name, new GenericObjectPool(null));
	}

	public static void unload(final String name) {
		if (log.isDebugEnabled())
			log.debug("unloading pool for database '" + name + "'");
		try {
			driver.closePool(name);
		} catch (final SQLException e) {
		}
	}

	public static void unloadAll() {
		final String[] names = driver.getPoolNames();
		if (names != null) {
			for (final String name : names)
				unload(name);
		}
	}

	public static boolean isLoaded(final String name) {
		final String[] names = driver.getPoolNames();
		if (names == null)
			return false;
		for (final String n : names) {
			if (n.compareTo(name) == 0)
				return true;
		}
		return false;
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

	public static boolean executeUpdate(final Connection conn, final String sql, Object... parameters) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, sql, parameters);
		try {
			return stmt.executeUpdate() == 1;
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

	public static Integer addRecord(final Connection conn, final String sql, Object... parameters) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, Statement.RETURN_GENERATED_KEYS, sql, parameters);
		try {
			if (stmt.executeUpdate() != 1) {
				if (log.isErrorEnabled())
					log.error("failed to add record");
				return null;
			}
			final ResultSet rsKey = stmt.getGeneratedKeys();
			rsKey.next();
			return rsKey.getInt(1);
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

	public static Integer addUniqueRecord(final Connection conn, final String sql, Object... parameters) throws SQLException {
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

	public static boolean isTableEmpty(final Connection conn, final String tableName) throws SQLException {
		final Statement stmt = conn.createStatement();
		try {
			final ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName + ";");
			return rs.next() && rs.getInt(1) == 0;
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

}
