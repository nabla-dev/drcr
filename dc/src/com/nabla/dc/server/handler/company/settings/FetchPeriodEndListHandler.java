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
import com.nabla.wapp.server.json.OdbcDateToJson;
import com.nabla.wapp.server.json.OdbcIdToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchPeriodEndListHandler extends AbstractFetchHandler<FetchAccountList> {

	private static final JsonFetch	fetcher = new JsonFetch(
		new OdbcIdToJson(),
		new OdbcStringToJson("name"),
		new OdbcDateToJson("end_date"),
		new OdbcBooleanToJson("sl_opened"),
		new OdbcBooleanToJson("pl_opened"),
		new OdbcBooleanToJson("nl_opened"),
		new OdbcBooleanToJson("bank_opened"),
		new OdbcBooleanToJson("visible")
	);

	@Override
	public FetchResult execute(final FetchAccountList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT * FROM (" +
"SELECT id, name, end_date, sl_opened, pl_opened, nl_opened, bank_opened, visible" +
" FROM period_end" +
" WHERE company_id=?" +
") AS dt {WHERE} {ORDER BY}"
			, cmd.getCompanyId());
	}

}
