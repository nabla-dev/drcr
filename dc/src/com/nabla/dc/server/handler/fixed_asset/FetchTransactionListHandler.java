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

import com.nabla.dc.shared.command.fixed_asset.FetchTransactionList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchTransactionListHandler extends AbstractFetchHandler<FetchTransactionList> {

	private static final SqlToJson	sql = new SqlToJson(
"SELECT t.id, p.name AS 'period', t.class, t.type, t.amount, t.depreciation_period" +
" FROM fa_transaction AS t INNER JOIN period_end AS p ON t.period_end_id=p.id" +
" WHERE t.fa_asset_id=?" +
" ORDER BY p.end_date"
	);

	@Override
	public FetchResult execute(final FetchTransactionList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return sql.fetch(cmd, ctx.getConnection(), cmd.getAssetId());
	}

}
