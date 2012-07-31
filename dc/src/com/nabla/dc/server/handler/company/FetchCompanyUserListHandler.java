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

import com.nabla.dc.shared.command.company.FetchCompanyUserList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIdToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.json.SimpleJsonFetch;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchCompanyUserListHandler extends AbstractFetchHandler<FetchCompanyUserList> {

	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT u.id, u.name, (c.company_id IS NOT NULL) AS 'active'" +
" FROM user AS u LEFT JOIN company_user AS c ON u.id=c.user_id AND c.company_id=?" +
" WHERE u.active=TRUE AND u.name NOT LIKE ? AND u.uname IS NOT NULL",
		new OdbcIdToJson(),
		new OdbcStringToJson("name"),
		new OdbcBooleanToJson("active")
	);

	@Override
	public FetchResult execute(final FetchCompanyUserList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.fetch(cmd, ctx.getConnection(), cmd.getCompanyId(), IRootUser.NAME);
	}

}