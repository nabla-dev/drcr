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
import com.nabla.wapp.shared.command.FetchUserDefinition;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchUserDefinitionHandler extends AbstractFetchHandler<FetchUserDefinition> {

	private static final SqlToJson	userSql = new SqlToJson(
"SELECT (privilege=FALSE) AS 'isFolder', isIncluded, TRUE AS 'isEnabled', id, -1 AS 'parentId', name FROM" +
" (SELECT r.id, r.name, r.privilege, TRUE AS 'isIncluded'" +
" FROM role AS r INNER JOIN user_definition AS d ON r.id=d.role_id" +
" WHERE d.user_id=? AND d.object_id IS NULL" +
" UNION" +
" SELECT r.id, r.name, r.privilege, FALSE AS 'isIncluded'" +
" FROM role AS r" +
" WHERE r.id NOT IN (SELECT d.role_id FROM user_definition AS d WHERE d.user_id=? AND d.object_id IS NULL)" +
") dt ORDER BY isIncluded DESC, privilege ASC, name ASC");

	private static final SqlToJson	objectUserSql = new SqlToJson(
"SELECT (privilege=FALSE) AS 'isFolder', isIncluded, TRUE AS 'isEnabled', id, -1 AS 'parentId', name FROM" +
" (SELECT r.id, r.name, r.privilege, TRUE AS 'isIncluded'" +
" FROM role AS r INNER JOIN user_definition AS d ON r.id=d.role_id" +
" WHERE d.user_id=? AND d.object_id=?" +
" UNION" +
" SELECT r.id, r.name, r.privilege, FALSE AS 'isIncluded'" +
" FROM role AS r" +
" WHERE r.id NOT IN (SELECT d.role_id FROM user_definition AS d WHERE d.user_id=? AND d.object_id=?)" +
") dt ORDER BY isIncluded DESC, privilege ASC, name ASC");

	private static final SqlToJson parentSql = new SqlToJson(
"SELECT (r.privilege=FALSE) as 'isFolder', FALSE AS 'isIncluded', FALSE AS 'isEnabled', r.id, d.role_id AS 'parentId', r.name" +
" FROM role_definition AS d INNER JOIN role AS r ON r.id=d.child_role_id" +
" WHERE d.role_id=?" +
" ORDER BY r.privilege ASC, r.name ASC");

	@Override
	public FetchResult execute(final FetchUserDefinition cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (cmd.getParentId() != null)
			return parentSql.serialize(cmd, ctx.getConnection(), cmd.getParentId());
		if (cmd.getObjectId() == null)
			return userSql.serialize(cmd, ctx.getConnection(), cmd.getUserId(), cmd.getUserId());
		return objectUserSql.serialize(cmd, ctx.getConnection(), cmd.getUserId(), cmd.getObjectId(), cmd.getUserId(), cmd.getObjectId());
	}

}
