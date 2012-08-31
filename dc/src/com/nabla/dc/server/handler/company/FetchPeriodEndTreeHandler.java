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

import com.nabla.dc.shared.command.company.FetchPeriodEndTree;
import com.nabla.dc.shared.model.company.IPeriodEnd;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.JsonFetch;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcDateToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;
import com.nabla.wapp.shared.model.IFieldReservedNames;

/**
 * @author nabla
 *
 */
public class FetchPeriodEndTreeHandler extends AbstractFetchHandler<FetchPeriodEndTree> {

	private static final JsonFetch	fetcher = new JsonFetch(
				new OdbcBooleanToJson(IFieldReservedNames.TREEGRID_IS_FOLDER),
				new OdbcStringToJson(IdField.NAME),
				new OdbcStringToJson(IFieldReservedNames.TREEGRID_PARENT_ID),
				new OdbcBooleanToJson(IFieldReservedNames.RECORD_ENABLED),
				new OdbcStringToJson(IPeriodEnd.NAME),
				new OdbcDateToJson(IPeriodEnd.END_DATE)
			);

	@Override
	public FetchResult execute(final FetchPeriodEndTree cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (cmd.getParentId() == null)
			return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT TRUE AS 'isFolder', CONCAT('f',t.id) AS 'id', NULL AS 'parentId', TRUE AS 'enabled', t.name, NULL AS 'end_date'" +
" FROM financial_year AS t" +
" WHERE t.company_id=?" +
" ORDER BY (SELECT p.end_date FROM period_end AS p WHERE p.financial_year_id=t.id LIMIT 1) DESC",
				cmd.getCompanyId());
		return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT FALSE as 'isFolder', CONCAT('p',t.id) AS 'id', CONCAT('f',t.financial_year_id) AS 'parentId', FALSE AS 'enabled', t.name, t.end_date" +
" FROM period_end AS t" +
" WHERE t.financial_year_id=?" +
" ORDER BY t.end_date ASC",
				cmd.getParentId());
	}

}
