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

import com.nabla.dc.shared.command.company.UpdateCompany;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author nabla
 *
 */
public class UpdateCompanyHandler extends AbstractUpdateHandler<UpdateCompany> {

	private static final UpdateStatement<UpdateCompany>	sql = new UpdateStatement<UpdateCompany>(UpdateCompany.class);

	@Override
	protected void update(UpdateCompany record, IUserSessionContext ctx) throws DispatchException, SQLException {
		sql.execute(ctx.getConnection(), record);
	}

}
