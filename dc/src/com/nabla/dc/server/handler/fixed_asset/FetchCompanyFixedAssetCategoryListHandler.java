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

import com.nabla.dc.shared.command.fixed_asset.FetchCompanyFixedAssetCategoryList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;


public class FetchCompanyFixedAssetCategoryListHandler extends AbstractFetchHandler<FetchCompanyFixedAssetCategoryList> {

	private static final SqlToJson	fetcher = new SqlToJson(
"SELECT r.id, t.name" +
" FROM fa_asset_category AS t INNER JOIN fa_company_asset_category AS r ON t.id=r.fa_asset_category_id" +
" WHERE t.active=TRUE AND t.uname IS NOT NULL AND r.company_id=? AND r.active=TRUE AND r.fa_fs_category_id IS NOT NULL"
		);

	@Override
	public FetchResult execute(final FetchCompanyFixedAssetCategoryList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getCompanyId());
	}

}
