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

import com.nabla.dc.shared.command.fixed_asset.FetchSplitAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchSplitAssetHandler extends AbstractFetchHandler<FetchSplitAsset> {

	private static final SqlToJson	sql = new SqlToJson(
"SELECT t.id, t.name AS 'nameA', t.name AS 'nameB', t.reference AS 'referenceA', t.reference AS 'referenceB'," +
"CONVERT((SELECT SUM(tt.amount) FROM fa_transaction AS tt WHERE tt.fa_asset_id=t.id AND tt.class='COST' AND tt.type IN ('OPENING','REVALUATION')), UNSIGNED) AS 'total'" +
" FROM fa_asset AS t" +
" WHERE t.id=?"
	);

	@Override
	public FetchResult execute(final FetchSplitAsset cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return sql.serialize(cmd, ctx.getConnection(), cmd.getId());
	}

}
