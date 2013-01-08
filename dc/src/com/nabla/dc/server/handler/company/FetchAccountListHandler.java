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
package com.nabla.dc.server.handler.company;

import java.sql.SQLException;

import com.nabla.dc.shared.command.company.FetchAccountList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;


public class FetchAccountListHandler extends AbstractFetchHandler<FetchAccountList> {

	private static final SqlToJson	rootSql = new SqlToJson(
"SELECT to_bool(IF(uname IS NULL,TRUE,FALSE)) AS 'deleted', id, code, name, cost_centre, department, balance_sheet, active" +
" FROM account" +
" WHERE company_id=?"
	);

	private static final SqlToJson	sql = new SqlToJson(
"SELECT to_bool(FALSE) AS 'deleted', id, code, name, cost_centre, department, balance_sheet, active" +
" FROM account" +
" WHERE company_id=? AND uname IS NOT NULL"
	);

	@Override
	public FetchResult execute(final FetchAccountList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return ctx.isRoot() ?
			rootSql.fetch(cmd, ctx.getConnection(), cmd.getCompanyId())
			:
			sql.fetch(cmd, ctx.getConnection(), cmd.getCompanyId());
	}

}
