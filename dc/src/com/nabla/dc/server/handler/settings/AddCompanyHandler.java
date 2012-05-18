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
package com.nabla.dc.server.handler.settings;

import java.sql.SQLException;

import com.nabla.dc.shared.command.settings.AddCompany;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.InsertStatement;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddCompanyHandler extends AbstractAddHandler<AddCompany> {

	public static final InsertStatement<AddCompany>	sql = new InsertStatement<AddCompany>(AddCompany.class);

	public AddCompanyHandler() {
		super();
	}

	@SuppressWarnings("static-access")
	@Override
	protected int add(final AddCompany record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		record.validate();
		final Integer id = sql.execute(ctx.getWriteConnection(), record);
		if (id == null)
			throw new ValidationException(record.NAME, CommonServerErrors.DUPLICATE_ENTRY);
		return id;
	}

}
