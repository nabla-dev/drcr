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
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAssetListHandler extends AbstractFetchHandler<FetchAssetList> {

	private static final SqlToJson	sql = new SqlToJson(
"SELECT t.id, t.name, t.reference, t.location, t.purchase_invoice" +
", c.name AS 'category'," +
" (SELECT SUM(tt.amount) FROM fa_transaction AS tt WHERE tt.fa_asset_id=t.id AND tt.class='COST' AND tt.type IN ('OPENING','REVALUATION')) AS 'i_cost'" +
", t.depreciation_period, t.acquisition_date, t.disposal_date, t.proceeds" +
" FROM fa_asset AS t INNER JOIN (" +
"fa_company_asset_category AS l INNER JOIN fa_asset_category AS c ON l.fa_asset_category_id=c.id" +
") ON t.fa_company_asset_category_id=l.id" +
" WHERE l.company_id=?"
	);

	@Override
	public FetchResult execute(final FetchAssetList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return sql.fetch(cmd, ctx.getConnection(), cmd.getCompanyId());
	}

}
