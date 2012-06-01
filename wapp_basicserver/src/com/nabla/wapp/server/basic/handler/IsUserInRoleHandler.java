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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.auth.RoleSetResult;
import com.nabla.wapp.shared.auth.command.IsUserInRole;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.StringSet;

/**
 * @author nabla
 *
 */
public class IsUserInRoleHandler extends AbstractHandler<IsUserInRole, RoleSetResult> {

	private static final String		userSql = "SELECT r.name" +
" FROM role AS r INNER JOIN (user_role AS d INNER JOIN user AS u ON d.user_id=u.id) ON r.id=d.role_id" +
" WHERE u.id=? AND d.object_id IS NULL AND r.uname IN (?);";

	private static final String		objectUserSql = "SELECT r.name" +
" FROM role AS r INNER JOIN (user_role AS d INNER JOIN user AS u ON d.user_id=u.id) ON r.id=d.role_id" +
" WHERE u.id=? AND (d.object_id IS NULL OR d.object_id=?) AND r.uname IN (?);";

	public IsUserInRoleHandler() {
		super();
	}

	@Override
	public RoleSetResult execute(final IsUserInRole cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// if user is 'root' then it supports all roles
		if (ctx.isRoot())
			return new RoleSetResult(cmd.getRoles());
		// otherwise test which role is supported
		final StringSet rslt = new StringSet();
		if (!cmd.getRoles().isEmpty()) {
			final PreparedStatement stmt = (cmd.getObjectId() == null) ?
				StatementFormat.prepare(ctx.getReadConnection(), userSql, ctx.getUserId(), cmd.getRoles())
			:
				StatementFormat.prepare(ctx.getReadConnection(), objectUserSql, ctx.getUserId(), cmd.getObjectId(), cmd.getRoles());
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					while (rs.next())
						rslt.add(rs.getString(1));
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		}
		return new RoleSetResult(rslt);
	}

}
