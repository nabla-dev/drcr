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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.Nullable;


public class UserPreference {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static StringResult load(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name) throws SQLException {
		final PreparedStatement stmt = (objectId == null) ?
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT state" +
" FROM user_preference" +
" WHERE user_id=? AND object_id IS NULL AND category=? AND name=?;",
ctx.getUserId(), group, name)
:
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT state" +
" FROM user_preference" +
" WHERE user_id=? AND object_id=? AND category=? AND name=?;",
ctx.getUserId(), objectId, group, name);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				return rs.next() ? new StringResult(rs.getString("state")) : null;
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	public static void save(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name, final String state) throws SQLException {
		if (state == null)
			remove(ctx, objectId, group, name);
		else if (objectId == null)
			Database.executeUpdate(ctx.getWriteConnection(),
"INSERT INTO user_preference (user_id,category,name,state) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE state=?;",
ctx.getUserId(), group, name, state, state);
		else
			Database.executeUpdate(ctx.getWriteConnection(),
"INSERT INTO user_preference (user_id,object_id,category,name,state) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE state=?;",
ctx.getUserId(), objectId, group, name, state, state);
	}

	public static void save(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name, final Integer state) throws SQLException {
		if (state == null)
			remove(ctx, objectId, group, name);
		else
			save(ctx, objectId, group, name, state.toString());
	}

	public static void save(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name, final Boolean state) throws SQLException {
		if (state == null)
			remove(ctx, objectId, group, name);
		else
			save(ctx, objectId, group, name, state.toString());
	}

	public static <E extends Enum<E>> void save(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name, final E state) throws SQLException {
		if (state == null)
			remove(ctx, objectId, group, name);
		else
			save(ctx, objectId, group, name, state.toString());
	}

	public static void save(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name, final Date state) throws SQLException {
		if (state == null)
			remove(ctx, objectId, group, name);
		else
			save(ctx, objectId, group, name, dateFormat.format(state));
	}

	public static void save(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name, final java.util.Date state) throws SQLException {
		if (state == null)
			remove(ctx, objectId, group, name);
		else
			save(ctx, objectId, group, name, dateFormat.format(state));
	}

	public static void remove(final IUserSessionContext ctx, @Nullable final Integer objectId, final String group, final String name) throws SQLException {
		if (objectId == null)
			Database.executeUpdate(ctx.getWriteConnection(),
"DELETE FROM user_preference WHERE user_id=? AND object_id IS NULL AND category=? AND name=?;",
ctx.getUserId(), group, name);
		else
			Database.executeUpdate(ctx.getWriteConnection(),
"DELETE FROM user_preference WHERE user_id=? AND object_id=? AND category=? AND name=?;",
ctx.getUserId(), objectId, group, name);
	}

}
