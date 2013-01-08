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
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.command.FetchRoleDefinition;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;


public class FetchRoleDefinitionHandler extends AbstractFetchHandler<FetchRoleDefinition> {

	private static final SqlToJson	parentSql = new SqlToJson(
"SELECT to_bool(privilege=FALSE) AS 'folder', to_bool(included) AS 'included', to_bool(TRUE) AS 'enabled', id, CONVERT(-1,SIGNED) AS 'parentId', name FROM" +
" (SELECT r.id, r.name, r.privilege, TRUE AS 'included'" +
" FROM role AS r INNER JOIN role_definition AS d ON r.id=d.child_role_id" +
" WHERE d.role_id=?" +
" UNION" +
" SELECT r.id, r.name, r.privilege, FALSE AS 'included'" +
" FROM role AS r" +
" WHERE r.id!=?" +
" AND r.id NOT IN (SELECT d.child_role_id FROM role_definition AS d WHERE d.role_id=?)" +
") dt ORDER BY included DESC, privilege ASC, name ASC");

	private static final SqlToJson	childSql = new SqlToJson(
"SELECT to_bool(r.privilege=FALSE) as 'folder', to_bool(FALSE) AS 'included', to_bool(FALSE) AS 'enabled', r.id, d.role_id AS 'parentId', r.name" +
" FROM role_definition AS d INNER JOIN role AS r ON r.id=d.child_role_id" +
" WHERE d.role_id=?" +
" ORDER BY r.privilege ASC, r.name ASC");

	@Override
	public FetchResult execute(final FetchRoleDefinition cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return (cmd.getParentId() == null) ?
			parentSql.serialize(cmd, ctx.getConnection(), cmd.getRoleId(),cmd.getRoleId(), cmd.getRoleId())
			:
			childSql.serialize(cmd, ctx.getConnection(), cmd.getParentId());
	}

}
