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
package com.nabla.dc.server.handler.company;

import java.sql.SQLException;

import com.nabla.dc.shared.command.company.UpdateCompanyUser;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;


public class UpdateCompanyUserHandler extends AbstractHandler<UpdateCompanyUser, StringResult> {

	public UpdateCompanyUserHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final UpdateCompanyUser record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (record.getActive()) {
			Database.executeUpdate(ctx.getWriteConnection(),
"INSERT IGNORE INTO company_user (company_id,user_id) VALUES(?,?);",
					record.getCompanyId(), record.getUserId());
		} else {
			Database.executeUpdate(ctx.getWriteConnection(),
"DELETE FROM company_user WHERE company_id=? AND user_id=?;",
					record.getCompanyId(), record.getUserId());
		}
		return null;
	}

}
