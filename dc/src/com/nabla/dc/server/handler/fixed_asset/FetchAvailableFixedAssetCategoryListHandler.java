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

import com.nabla.dc.shared.command.fixed_asset.FetchAvailableFixedAssetCategoryList;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.OdbcIdToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.json.SimpleJsonFetch;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAvailableFixedAssetCategoryListHandler extends AbstractFetchHandler<FetchAvailableFixedAssetCategoryList> {

	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT id, name FROM" +
" (SELECT a.id, a.name" +
" FROM fa_asset_category AS a INNER JOIN fa_company_asset_category AS c ON a.id=c.fa_asset_category_id" +
" WHERE c.company_id=? AND c.fa_fs_category_id IS NULL" +
" UNION" +
" SELECT a.id, a.name" +
" FROM fa_asset_category AS a LEFT JOIN fa_company_asset_category AS c ON a.id=c.fa_asset_category_id" +
" WHERE c.id IS NULL" +
") dt ORDER BY name ASC",
		new OdbcIdToJson(),
		new OdbcStringToJson(IFixedAssetCategory.NAME)
	);

	@Override
	public FetchResult execute(final FetchAvailableFixedAssetCategoryList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getCompanyId());
	}

}
