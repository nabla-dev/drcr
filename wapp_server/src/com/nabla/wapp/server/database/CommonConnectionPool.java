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
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.nabla.wapp.server.general.Assert;

/**
 * @author nabla64
 * use JOCL file to get database pool
 */
public class CommonConnectionPool implements IConnectionPool {

	public static final String			DATABASE_DRIVER_NAME = "database_driver_name";
	private static final Log				log = LogFactory.getLog(CommonConnectionPool.class);
	private static final PoolingDriver	driver = new PoolingDriver();
	private final String					url;
	
	/**
	 * Constructor
	 * @param dbName			- database name as defined in pool
	 * @param serverContext		- web app context
	 * @throws SQLException 
	 */
	public CommonConnectionPool(final String dbName, final ServletContext serverContext) throws SQLException {
		Assert.argumentNotNull(dbName, "Have you set the database name in your web.xml file?");
		Assert.argumentNotNull(serverContext);
		
		url = "jdbc:apache:commons:dbcp:/" + dbName;
		if (!isLoaded(dbName)) {
			load(dbName);
			final String driverName = serverContext.getInitParameter(DATABASE_DRIVER_NAME);
			Assert.notNull(driverName, "Have you set the database driver name in your web.xml file?");				
			try {
				Class.forName(driverName);
			} catch (ClassNotFoundException e) {
				if (log.isDebugEnabled())
					log.debug("fail to find database driver class '" + driverName + "'", e);
				throw new SQLException("fail to find database driver class '" + driverName + "'");
			}
		}
	}

	@Override
	public Connection get() throws SQLException {
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
/*
	public static void unload(final String name) {
		if (log.isDebugEnabled())
			log.debug("unloading pool for database '" + name + "'");
		try {
			driver.closePool(name);
		} catch (final SQLException e) {}
	}

	public static void unloadAll() {
		final String[] names = driver.getPoolNames();
		if (names != null) {
			for (final String name : names)
				unload(name);
		}
	}
*/
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

}
