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

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.FetchBalanceSheetCategoryDefinition;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.server.model.FetchRecordRequest;

/**
 * @author nabla
 *
 */
public class FetchBalanceSheetCategoryDefinitionHandler extends AbstractOperationHandler<FetchBalanceSheetCategoryDefinition, FetchRecordRequest> {

	public FetchBalanceSheetCategoryDefinitionHandler() {
		super(false, IPrivileges.BS_CATEGORY_EDIT);
	}

	@Override
	protected String execute(final FetchRecordRequest request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return request.serialize(ctx.getReadConnection(),
"SELECT isSelected, id, name FROM" +
" (SELECT id, name, 'true' AS 'isSelected' FROM bs_category_active_asset_category" +
" WHERE bs_category_id=?" +
" UNION" +
" SELECT id, name, 'false' AS 'isSelected' FROM live_asset_category" +
" WHERE id NOT IN (SELECT asset_category_id FROM bs_category_definition WHERE bs_category_id=?) AND active='true'" +
") dt {WHERE} ORDER BY isSelected DESC, name ASC {AND ORDER BY}", request.recordId, request.recordId);
	}

}
