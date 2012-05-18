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
package com.nabla.wapp.server.basic.general;

import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.server.auth.IUserSessionContextProvider;
import com.nabla.wapp.server.auth.UserSessionContext;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadOnlyDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.general.UserSession;

/**
 * @author nabla

 *
 */
@Singleton
public class UserSessionContextProvider implements IUserSessionContextProvider {

	private final IDatabase		readDb;
	private final IDatabase		writeDb;

	@Inject
	public UserSessionContextProvider(@IReadOnlyDatabase final IDatabase readDb, @IReadWriteDatabase final IDatabase writeDb) {
		this.readDb = readDb;
		this.writeDb = writeDb;
	}

	@Override
	public UserSessionContext get(final UserSession session, boolean writeContext) throws SQLException {
		return new UserSessionContext(session, readDb.getConnection(), writeContext ? writeDb.getConnection() : null);
	}

}
