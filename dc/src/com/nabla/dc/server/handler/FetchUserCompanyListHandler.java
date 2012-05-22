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
package com.nabla.dc.server.handler;

import java.sql.SQLException;

import com.nabla.dc.shared.command.FetchUserCompanyList;
import com.nabla.dc.shared.model.IUserCompany;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.JsonFetch;
import com.nabla.wapp.server.json.OdbcIntToJson;
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
		new OdbcIntToJson(IdField.NAME),
		new OdbcStringToJson(IUserCompany.NAME),
		new OdbcStringToJson(IUserCompany.LOGO)
	);

	public FetchUserCompanyListHandler() {
		super();
	}

	@Override
	public FetchResult execute(final FetchUserCompanyList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (ctx.isRoot())
			return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT c.id, c.name, IF(c.logo_id IS NOT NULL,CONCAT('/dc/image?id=',c.logo_id), 'default_logo.png') AS 'image'" +
" FROM company AS c" +
" WHERE c.active=TRUE AND c.uname IS NOT NULL");
		return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT c.id, c.name, IF(c.logo_id IS NOT NULL,CONCAT('/dc/image?id=',c.logo_id), 'default_logo.png') AS 'image'" +
" FROM company AS c INNER JOIN company_user AS u ON c.id=u.company_id" +
" WHERE c.active=TRUE AND c.uname IS NOT NULL AND u.user_id=?",
			ctx.getUserId());			
	}

}
