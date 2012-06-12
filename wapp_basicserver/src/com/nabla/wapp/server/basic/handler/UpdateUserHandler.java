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

import java.sql.SQLException;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.command.UpdateUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class UpdateUserHandler extends AbstractHandler<UpdateUser, StringResult> {

	private static final UpdateStatement<UpdateUser>	sql = new UpdateStatement<UpdateUser>(UpdateUser.class);

	public UpdateUserHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final UpdateUser record, final IUserSessionContext ctx) throws DispatchException, SQLException {
	//	record.validate();
		sql.execute(ctx.getConnection(), record);
		return null;
	}

}
