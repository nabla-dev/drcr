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
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.general.UserSession;
import com.nabla.wapp.shared.auth.ILoginUserRemoteService;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
@Singleton
public class LoginUserService extends RemoteServiceServlet implements ILoginUserRemoteService {

	private static final long		serialVersionUID = 1L;
	private static final Log		log = LogFactory.getLog(LoginUserService.class);

	private final IDatabase		db;

	@Inject
	public LoginUserService(@IReadWriteDatabase final IDatabase db) {
		this.db = db;
	}

	@Override
	public String execute(String userName, String password) {
		final ValidationException x = new ValidationException();
		IUser.NAME_CONSTRAINT.validate("user name", userName, x);
		IUser.PASSWORD_CONSTRAINT.validate("password", password, x);
		if (x.isEmpty()) {
			try {
				final Connection conn = db.getConnection();
				try {
					final UserManager userManager = new UserManager(conn);
					final Integer userId = userManager.isValidSession(userName, password);
					if (userId != null) {
						userManager.onUserLogged(userId);
						return UserSession.save(this.getThreadLocalRequest(), userId, userName);
					}
				} finally {
					Database.close(conn);
				}
			} catch (final SQLException e) {
				if (log.isErrorEnabled())
					log.error("SQL error " + e.getErrorCode() + "-"	+ e.getSQLState(), e);
			}
		}
		return null;
	}

	@Override
	public Void reset() {
		UserSession.clear(this.getThreadLocalRequest());
		return null;
	}

}
