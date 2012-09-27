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

import com.nabla.dc.shared.command.fixed_asset.FetchAssetProperties;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAssetPropertiesHandler extends AbstractFetchHandler<FetchAssetProperties> {

	private static final SqlToJson	fetcher = new SqlToJson(
"SELECT t.id, t.name, t.fa_company_asset_category_id AS 'category', t.reference, t.location" +
", t.acquisition_date, t.acquisition_type, t.purchase_invoice" +
", (SELECT tt.amount FROM fa_transaction AS tt WHERE tt.fa_asset_id=t.id AND tt.class='COST' AND tt.type='OPENING') AS 'i_cost'" +
", o.amount AS 'initial_accumulated_depreciation', o.depreciation_period AS 'initial_depreciation_period'" +
", t.depreciation_period" +
", (SELECT SUM(tt.amount) FROM fa_transaction AS tt WHERE tt.fa_asset_id=t.id) AS 'i_residual_value'" +
", t.disposal_date, t.disposal_type, t.proceeds" +
" FROM fa_asset AS t LEFT JOIN fa_transaction AS o ON (t.id=o.fa_asset_id AND o.class='DEP' AND o.type='OPENING')" +
" WHERE t.id=?"
	);

	@Override
	public FetchResult execute(final FetchAssetProperties cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getId());
	}

}
