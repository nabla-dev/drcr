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

import com.nabla.dc.shared.command.fixed_asset.FetchAssetList;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
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
public class FetchAssetListHandler extends AbstractFetchHandler<FetchAssetList> {

	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT t.id, t.name, t.reference, t.location, t.purchase_invoice, c.name AS 'category', tt.value AS 'cost', t.depreciation_period, t.acquisition_date, t.disposal_date, t.proceeds" +
" FROM fa_asset AS t" +
" WHERE register_id=? {AND WHERE} {ORDER BY}",
		new OdbcIdToJson(),
		new OdbcStringToJson(IAsset.NAME),
		new OdbcStringToJson(IAsset.REFERENCE),
		new OdbcStringToJson(IAsset.LOCATION),
		new OdbcStringToJson(IAsset.PURCHASE_INVOICE),
		new OdbcStringToJson(IAsset.CATEGORY),
		new OdbcBooleanToJson("balance_sheet"),
		new OdbcBooleanToJson("active")
	);

	@Override
	public FetchResult execute(final FetchAssetList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.fetch(cmd, ctx.getConnection(), cmd.getCompanyId());
	}

}
