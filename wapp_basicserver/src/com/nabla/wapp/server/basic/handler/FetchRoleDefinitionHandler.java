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
package com.nabla.wapp.server.basic.handler;

import java.sql.SQLException;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.JsonFetch;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIdToJson;
import com.nabla.wapp.server.json.OdbcIntToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.command.FetchRoleDefinition;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;
import com.nabla.wapp.shared.model.IFieldReservedNames;

/**
 * @author nabla
 *
 */
public class FetchRoleDefinitionHandler extends AbstractFetchHandler<FetchRoleDefinition> {

	private static final JsonFetch	fetcher = new JsonFetch(
				new OdbcBooleanToJson(IFieldReservedNames.TREEGRID_IS_FOLDER),
				new OdbcBooleanToJson("isIncluded"),
				new OdbcBooleanToJson(IFieldReservedNames.RECORD_ENABLED),
				new OdbcIdToJson(),
				new OdbcIntToJson(IFieldReservedNames.TREEGRID_PARENT_ID),
				new OdbcStringToJson("name")
			);

	public FetchRoleDefinitionHandler() {
		super();
	}

	@Override
	public FetchResult execute(final FetchRoleDefinition cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (cmd.getParentId() == null) {
			return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT (privilege=FALSE) AS 'isFolder', isIncluded, TRUE AS 'enabled', id, -1 AS 'parentId', name FROM" +
" (SELECT r.id, r.name, r.privilege, TRUE AS 'isIncluded'" +
" FROM role AS r INNER JOIN role_definition AS d ON r.id=d.child_role_id" +
" WHERE d.role_id=?" +
" UNION" +
" SELECT r.id, r.name, r.privilege, FALSE AS 'isIncluded'" +
" FROM role AS r" +
" WHERE r.id!=?" +
" AND r.id NOT IN (SELECT d.child_role_id FROM role_definition AS d WHERE d.role_id=?)" +
") dt ORDER BY isIncluded DESC, privilege ASC, name ASC",
				cmd.getRoleId(),
				cmd.getRoleId(),
				cmd.getRoleId());
		}
		return fetcher.serialize(cmd, ctx.getConnection(),
"SELECT (r.privilege=FALSE) as 'isFolder', FALSE AS 'isIncluded', FALSE AS 'enabled', r.id, d.role_id AS 'parentId', r.name" +
" FROM role_definition AS d INNER JOIN role AS r ON r.id=d.child_role_id" +
" WHERE d.role_id=?" +
" ORDER BY r.privilege ASC, r.name ASC",
				cmd.getParentId());
	}

}
