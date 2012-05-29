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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.dc.shared.command.RoleName;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author FNorais
 *
 */
public class RoleNameHandler extends AbstractHandler<RoleName, StringResult> {

	@Override
	public StringResult execute(final RoleName cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT name FROM role WHERE id=?;",
				cmd.getRoleId());
		try {
			final ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return new StringResult(rs.getString(1));
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
		throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
	}
	
}
