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

import com.nabla.dc.shared.command.fixed_asset.FetchAssetDisposal;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAssetDisposalHandler extends AbstractFetchHandler<FetchAssetDisposal> {

	private static final SqlToJson	sql = new SqlToJson(
"SELECT t.id, t.name" +
", IFNULL(t.disposal_date, (SELECT p.state FROM user_preference AS p WHERE p.user_id=? AND p.object_id=c.company_id AND p.category=? AND p.name='disposal_date')) AS 'dt_disposal_date'" +
", IFNULL(t.disposal_type, (SELECT p.state FROM user_preference AS p WHERE p.user_id=? AND p.object_id=c.company_id AND p.category=? AND p.name='disposal_type')) AS 'disposal_type'" +
", proceeds" +
" FROM fa_company_asset_category AS c INNER JOIN (" +
"fa_asset AS t LEFT JOIN fa_transaction AS o ON (t.id=o.fa_asset_id AND o.class='DEP' AND o.type='OPENING')" +
") ON c.id=t.fa_company_asset_category_id" +
" WHERE t.id=?"
	);

	@Override
	public FetchResult execute(final FetchAssetDisposal cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return sql.serialize(cmd, ctx.getConnection(),
ctx.getUserId(), IAsset.DISPOSAL_PREFERENCE_GROUP, ctx.getUserId(), IAsset.DISPOSAL_PREFERENCE_GROUP, cmd.getId());
	}

}
