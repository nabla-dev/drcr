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
package com.nabla.wapp.server.basic.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.minidev.json.JSONObject;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * The <code>AbstractGetDefaultValuesHandler</code> object is used to
 *
 */
public abstract class AbstractGetDefaultValuesHandler<A extends IAction<StringResult>> extends AbstractHandler<A, StringResult> {

	protected StringResult getDefaultValues(final String group, final IUserSessionContext ctx) throws SQLException {
		return executeQuery(StatementFormat.prepare(ctx.getReadConnection(),
"SELECT name AS 'field', state AS 'default'" +
" FROM user_preference" +
" WHERE user_id=? AND category=?;",
ctx.getUserId(), group));
	}

	protected static StringResult executeQuery(final PreparedStatement stmt) throws SQLException {
		try {
			final JsonResponse json = new JsonResponse();
			final JSONObject record = new JSONObject();
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					return null;
				do {
					record.put(rs.getString("field"), rs.getString("default"));
				} while (rs.next());
			} finally {
				rs.close();
			}
			json.add(record);
			return json.toStringResult();
		} finally {
			stmt.close();
		}
	}

}
