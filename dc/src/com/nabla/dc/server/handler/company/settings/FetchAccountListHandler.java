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
package com.nabla.dc.server.handler.company.settings;

import java.sql.SQLException;

import com.nabla.dc.shared.command.company.settings.FetchAccountList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.JsonFetch;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIdToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAccountListHandler extends AbstractFetchHandler<FetchAccountList> {

	private static final JsonFetch	fetcher = new JsonFetch(
		new OdbcIdToJson(),
		new OdbcStringToJson("code"),
		new OdbcStringToJson("name"),
		new OdbcStringToJson("cost_centre"),
		new OdbcStringToJson("department"),
		new OdbcBooleanToJson("balance_sheet"),
		new OdbcBooleanToJson("active")
	);

	@Override
	public FetchResult execute(final FetchAccountList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), ctx.isRoot() ?
"SELECT * FROM (" +
"SELECT id, code, name, cost_centre, department, balance_sheet, active" +
" FROM account" +
" WHERE company_id=? AND uname IS NOT NULL" +
") AS dt {WHERE} {ORDER BY}"
			:
"SELECT * FROM (" +
"SELECT id, code, name, cost_centre, department, balance_sheet, active" +
" FROM account" +
" WHERE company_id=?" +
") AS dt {WHERE} {ORDER BY}"
			, cmd.getCompanyId());
	}

}
