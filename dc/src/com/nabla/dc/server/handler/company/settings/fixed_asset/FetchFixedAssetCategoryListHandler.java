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
package com.nabla.dc.server.handler.company.settings.fixed_asset;

import java.sql.SQLException;

import com.nabla.dc.shared.command.company.settings.fixed_asset.FetchFixedAssetCategoryList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIntToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.json.SimpleJsonFetch;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchFixedAssetCategoryListHandler extends AbstractFetchHandler<FetchFixedAssetCategoryList> {

	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT t.id, t.name, IF(b.id IS NOT NULL, b.name, NULL) AS 'bs_category', IF(c.company_id IS NOT NULL, c.active, FALSE) AS 'active'" +
" FROM fa_asset_category AS t LEFT JOIN (fa_company_asset_category AS c INNER JOIN fa_bs_category AS b ON c.fa_bs_category_id=b.id)ON t.id=c.fa_asset_category_id AND c.company_id=?" +
" WHERE t.active=TRUE AND t.uname IS NOT NULL",
		new OdbcIntToJson("id"),
		new OdbcStringToJson("name"),
		new OdbcStringToJson("bs_category"),
		new OdbcBooleanToJson("active")
	);

	@Override
	public FetchResult execute(final FetchFixedAssetCategoryList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.fetch(cmd, ctx.getConnection(), cmd.getCompanyId());
	}

}
