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

/**
 * @author nabla
 *
 */
public class ConnectionTransactionGuard {

	private final boolean		autoCommit;
	private final Connection	conn;
	private Boolean				success = false;

	/**
	 * Start transactions block i.e. switch connection auto commit to false
	 * You need to call close() method in a <b>finally</b> block to either rollback or commit transactions
	 * @param conn	- connection handle
	 * @throws SQLException
	 */
	public ConnectionTransactionGuard(final Connection conn) throws SQLException {
		this.conn = conn;
		autoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
	}

	/**
	 * Return connection association in transaction
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Notify guard that transactions should be committed
	 * You need to call this method before your <b>finally</b> block
	 * @return success
	 */
	public boolean setSuccess() {
		return setSuccess(true);
	}

	/**
	 * Notify guard that transactions should be committed or rollbacked
	 * You need to call this method before your <b>finally</b> block
	 * @param success	- true to commit or false to rollback
	 * @return success
	 */
	public boolean setSuccess(boolean success) {
		this.success = success;
		return success;
	}

	/**
	 * To be called in your <b>finally</b> block
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (success != null) {
			try {
				if (success)
					conn.commit();
				else
					conn.rollback();
			} finally {
				success = null;
				conn.setAutoCommit(autoCommit);
			}
		}
	}
}
