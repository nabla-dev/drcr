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

import com.nabla.dc.shared.command.fixed_asset.FetchBalanceSheetCategoryDefinition;
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
public class FetchBalanceSheetCategoryDefinitionHandler extends AbstractFetchHandler<FetchBalanceSheetCategoryDefinition> {

	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT t.id, t.name, (tt.company_id IS NOT NULL) AS 'active'" +
" FROM fa_asset_category AS t LEFT JOIN fa_bs_category_definition AS tt ON t.id=tt.fa_asset_category_id AND tt.fa_bs_category_id=?" +
" WHERE t.active=TRUE AND t.uname IS NOT NULL",
		new OdbcIntToJson("id"),
		new OdbcStringToJson("name"),
		new OdbcBooleanToJson("active")
	);

	@Override
	public FetchResult execute(final FetchBalanceSheetCategoryDefinition cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getBalanceSheetCategoryId());
	}

}
