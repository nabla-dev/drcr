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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.general.Assert;

/**
 * @author nabla64
 * use TOMCAT to get database pool
 */
public class TomcatConnectionPool implements IConnectionPool {

	private static final Log				log = LogFactory.getLog(Database.class);
	private final DataSource				dataSource;
	
	/**
	 * Constructor
	 * @param dbName			- database name as defined in pool
	 * @throws SQLException 
	 */
	public TomcatConnectionPool(final String dbName) throws SQLException {
		Assert.argumentNotNull(dbName, "Have you set the database name in your web.xml file?");
		
		try {
			final Context initContext  = new InitialContext();
	        final Context ctx  = (Context)initContext.lookup("java:/comp/env");
	        dataSource = (DataSource)ctx.lookup("jdbc/" + dbName);
		} catch (NamingException e) {
			if (log.isDebugEnabled())
				log.debug("fail to get datasource '" + dbName + "'", e);
			throw new SQLException("fail to get database '" + dbName + "'");
		}			
	}

	@Override
	public Connection get() throws SQLException {
		return dataSource.getConnection();
	}

}
