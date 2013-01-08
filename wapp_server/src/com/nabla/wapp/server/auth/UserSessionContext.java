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
package com.nabla.wapp.server.auth;

import java.sql.Connection;
import java.util.Locale;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.UserSession;

/**
 * The <code>UserSessionContext</code> object is a basic IUserSessionContext
 *
 */
public class UserSessionContext implements IUserSessionContext {

	private final UserSession	userSession;
	private final Connection	readConn;
	private final Connection	writeConn;

	public UserSessionContext(final UserSession userSession, final Connection readConn, final Connection writeConn) {
		this.userSession = userSession;
		this.readConn = readConn;
		this.writeConn = writeConn;
	}

	@Override
	public void close() {
		Database.close(readConn);
		if (writeConn != null)
			Database.close(writeConn);
	}

	@Override
	public Integer getUserId() {
		return userSession.getUserId();
	}

	@Override
	public boolean isRoot() {
		return userSession.isRoot();
	}

	@Override
	public Connection getReadConnection() {
		return readConn;
	}

	@Override
	public Connection getWriteConnection() {
		return writeConn;
	}

	@Override
	public Connection getConnection() {
		return (writeConn == null) ? readConn : writeConn;
	}

	@Override
	public Locale getLocale() {
		return userSession.getLocale();
	}

	@Override
	public String getSessionId() {
		return userSession.getSessionId();
	}

}
