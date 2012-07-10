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
import com.nabla.fixed_assets.shared.command.FetchBalanceSheetCategoryList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.server.model.FetchRequest;

/**
 * @author nabla
 *
 */
public class FetchBalanceSheetCategoryListHandler extends AbstractOperationHandler<FetchBalanceSheetCategoryList, FetchRequest> {

	public FetchBalanceSheetCategoryListHandler() {
		super(false, IPrivileges.BS_CATEGORY_VIEW);
	}

	@Override
	protected String execute(final FetchRequest request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return request.serialize(ctx.getReadConnection(), ctx.isRoot() ?
"SELECT * FROM (" +
"SELECT sf_toBool(uname IS NULL) as 'deleted', id, name, sf_toBool(active) AS 'active'" +
" FROM bs_category" +
") AS dt {WHERE} {ORDER BY}"
		:
"SELECT id, name, active FROM live_bs_category {WHERE} {ORDER BY}");
	}

}
