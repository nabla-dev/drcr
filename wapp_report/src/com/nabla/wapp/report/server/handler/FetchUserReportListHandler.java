/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.server.handler;

import java.sql.SQLException;

import com.nabla.wapp.report.shared.command.FetchUserReportList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchUserReportListHandler extends AbstractFetchHandler<FetchUserReportList> {

	private static final SqlToJson	sql = new SqlToJson(
"SELECT r.id, r.name" +
" FROM report AS r INNER JOIN user_role ON r.role_id=user_role.role_id" +
" WHERE user_role.user_id=?"
	);

	@Override
	public FetchResult execute(final FetchUserReportList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return sql.fetch(cmd, ctx.getConnection(), ctx.getUserId());
	}

}
