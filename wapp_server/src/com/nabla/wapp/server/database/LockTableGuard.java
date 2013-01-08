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
import java.sql.Statement;

public class LockTableGuard {

	private final Statement lockStmt;

	/**
	 * Start lock tables block
	 * You need to call close() method in a <b>finally</b> block to unlock tables
	 * @param conn		- connection handle
	 * @param tables	- comma separated list of tables
	 * @throws SQLException
	 */
	public LockTableGuard(final Connection conn, final String tables) throws SQLException {
		lockStmt = conn.createStatement();
		try {
			lockStmt.execute("LOCK TABLES " + tables + ";");
		} catch (SQLException e) {
			Database.close(lockStmt);
			throw e;
		}
	}

	/**
	 * To be called in your <b>finally</b> block
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		try {
			lockStmt.execute("UNLOCK TABLES;");
		} finally {
			lockStmt.close();
		}
	}
}
