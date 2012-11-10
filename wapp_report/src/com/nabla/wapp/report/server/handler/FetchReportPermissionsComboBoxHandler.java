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

import com.nabla.wapp.report.shared.command.FetchReportPermissionsComboBox;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchReportPermissionsComboBoxHandler extends AbstractFetchHandler<FetchReportPermissionsComboBox> {

	private static final SqlToJson sql = new SqlToJson(
"SELECT id, name" +
" FROM role" +
" WHERE uname IS NOT NULL" +
" ORDER BY name"
	);

	@Override
	public FetchResult execute(final FetchReportPermissionsComboBox cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return sql.fetch(cmd, ctx.getConnection());
	}

}
