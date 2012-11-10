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
package com.nabla.wapp.server.basic.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.auth.UserManager;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.command.AddUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddUserHandler extends AbstractAddHandler<AddUser> {

	@Override
	protected int add(AddUser record, IUserSessionContext ctx) throws DispatchException, SQLException {
		final Integer id = new UserManager(ctx.getWriteConnection()).addUser(record.getName(), record.getPassword());
		if (id == null)
			throw new ValidationException(IUser.NAME, CommonServerErrors.DUPLICATE_ENTRY);
		return id;
	}

	@Override
	protected void generateResponse(final JsonResponse json, @SuppressWarnings("unused") final AddUser record, int recordId, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// return more info than just ID
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT id, active, created FROM user WHERE id=?;", recordId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				json.putNext(rs);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

}
