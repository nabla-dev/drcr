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

import com.nabla.dc.shared.command.company.FetchCompanyList;
import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.JsonFetch;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIdToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;
import com.nabla.wapp.shared.model.IFieldReservedNames;

/**
 * @author nabla
 *
 */
public class FetchCompanyListHandler extends AbstractFetchHandler<FetchCompanyList> {

	private static final JsonFetch	fetcher = new JsonFetch(
		new OdbcBooleanToJson(IFieldReservedNames.RECORD_DELETED),
		new OdbcIdToJson(),
		new OdbcStringToJson(ICompany.NAME),
		new OdbcBooleanToJson(ICompany.ACTIVE)
	);

	@Override
	public FetchResult execute(final FetchCompanyList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.fetch(cmd, ctx.getConnection(), ctx.isRoot() ?
"SELECT IF(uname IS NULL,TRUE,FALSE) AS 'deleted', id, name, active" +
" FROM company"
	:
"SELECT FALSE AS 'deleted', id, name, active" +
" FROM company" +
" WHERE uname IS NOT NULL");
	}

}
