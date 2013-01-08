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
import com.nabla.wapp.server.auth.UserManager;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.command.AddRole;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IRole;
import com.nabla.wapp.shared.model.ValidationException;


public class AddRoleHandler extends AbstractAddHandler<AddRole> {

	@Override
	protected int add(final AddRole record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final Integer id = new UserManager(ctx.getConnection()).addRole(record.getName());
		if (id == null)
			throw new ValidationException(IRole.NAME, CommonServerErrors.DUPLICATE_ENTRY);
		return id;
	}

}
