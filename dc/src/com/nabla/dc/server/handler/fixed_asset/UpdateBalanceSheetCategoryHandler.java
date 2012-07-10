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
package com.nabla.dc.server.handler.fixed_asset;

import java.sql.SQLException;

import com.nabla.wapp.shared.dispatch.DispatchException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.UpdateBalanceSheetCategory;
import com.nabla.fixed_assets.shared.model.IBalanceSheetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.general.SimpleString;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateBalanceSheetCategoryHandler extends AbstractOperationHandler<UpdateBalanceSheetCategory, UpdateBalanceSheetCategoryHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="bs_category")
	static class Record {

		@Element
		@IRecordField(id=true)
		Integer	id;
		@Element(required=false)
		@IRecordField(unique=true)
		SimpleString		name;
		@IRecordField
		String				uname;
		@Element(required=false)
		@IRecordField
		Boolean				active;

		@Validate
		public void validate() throws ValidationException {
			IBalanceSheetCategory.NAME_CONSTRAINT.validate(IBalanceSheetCategory.NAME, name);
			if (name != null)
				uname = name.getValue().toUpperCase();
		}

	}

	private final UpdateStatement<Record>	sql = new UpdateStatement<Record>(Record.class);

	public UpdateBalanceSheetCategoryHandler() {
		super(true, IPrivileges.BS_CATEGORY_EDIT);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		sql.execute(ctx.getWriteConnection(), request);
		return null;
	}

}
