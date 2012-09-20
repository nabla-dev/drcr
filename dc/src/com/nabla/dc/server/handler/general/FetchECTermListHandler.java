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
package com.nabla.dc.server.handler.general;

import java.sql.SQLException;

import com.nabla.dc.shared.command.general.FetchECTermList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchECTermListHandler extends AbstractFetchHandler<FetchECTermList> {

	private static final SqlToJson	rootSql = new SqlToJson(
"SELECT IF(uname IS NULL,TRUE,FALSE) AS 'b_deleted', id, name, active AS 'b_active'" +
" FROM ec_term"
	);

	private static final SqlToJson	sql = new SqlToJson(
"SELECT FALSE AS 'b_deleted', id, name, active AS 'b_active'" +
" FROM ec_term" +
" WHERE uname IS NOT NULL"
	);

	@Override
	public FetchResult execute(final FetchECTermList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return ctx.isRoot() ?
			rootSql.fetch(cmd, ctx.getConnection())
			:
			sql.fetch(cmd, ctx.getConnection());
	}

}
