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

import com.nabla.dc.shared.command.FetchImportErrorList;
import com.nabla.dc.shared.model.IImportError;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.OdbcIntToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.json.SimpleJsonFetch;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchImportErrorListHandler extends AbstractFetchHandler<FetchImportErrorList> {

	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT line_no, field, error" +
" FROM import_error" +
" WHERE import_data_id=? {AND WHERE} {ORDER BY}",
		new OdbcIntToJson(IImportError.LINE),
		new OdbcStringToJson(IImportError.FIELD),
		new OdbcStringToJson(IImportError.ERROR)
	);

	@Override
	public FetchResult execute(final FetchImportErrorList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getBatchId());
	}

}
