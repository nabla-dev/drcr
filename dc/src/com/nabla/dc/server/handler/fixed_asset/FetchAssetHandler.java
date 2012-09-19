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

import com.nabla.dc.shared.command.fixed_asset.FetchAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAssetHandler extends AbstractFetchHandler<FetchAsset> {

	private static final SqlToJson	fetcher = new SqlToJson(
"SELECT t.id, t.name, c.fa_asset_category_id AS 'asset_category_id', t.reference, t.location" +
", t.acquisition_date, t.acquisition_type, t.purchase_invoice" +
", (SELECT tt.amount FROM fa_transaction AS tt WHERE tt.fa_asset_id=t.id AND tt.class='COST' AND tt.type='OPENING') AS 'cost'" +
", init.amount AS 'initial_accumulated_depreciation', init.depreciation_period AS 'initial_depreciation_period'" +
", t.depreciation_period" +
", (SELECT SUM(tt.amount) FROM transaction AS tt WHERE tt.fa_asset_id=a.id) AS 'residual_value'" +
" FROM fa_asset AS t INNER JOIN fa_company_asset_category AS c ON t.fa_company_asset_category_id=c.id" +
" INNER JOIN transaction AS init ON a.id=init.fa_asset_id AND init.class='DEP' AND init.type='OPENING'" +
" WHERE t.id=?"
	);

	@Override
	public FetchResult execute(final FetchAsset cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getId());
	}
}
