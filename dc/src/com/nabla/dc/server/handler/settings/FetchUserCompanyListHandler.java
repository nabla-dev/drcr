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
package com.nabla.dc.server.handler.settings;

import java.sql.SQLException;

import com.nabla.dc.shared.command.settings.FetchUserCompanyList;
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
public class FetchUserCompanyListHandler extends AbstractFetchHandler<FetchUserCompanyList> {

	private static final JsonFetch	fetcher = new JsonFetch(
		new OdbcIdToJson(),
		new OdbcStringToJson("name"),
		new OdbcBooleanToJson("active")
	);

	public FetchUserCompanyListHandler() {
		super();
	}

	@Override
	public FetchResult execute(final FetchUserCompanyList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT * FROM (" +
"SELECT c.id, c.name, (l.company_id IS NOT NULL) AS 'active'" +
" FROM company AS c LEFT JOIN company_user AS l ON c.id=l.company_id AND l.user_id=?" +
" WHERE c.active=TRUE AND c.uname IS NOT NULL" +
") AS dt {WHERE} {ORDER BY}",
			cmd.getUserId());			
	}
}
