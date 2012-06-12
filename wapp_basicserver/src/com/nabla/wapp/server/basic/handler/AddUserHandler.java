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
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.auth.UserManager;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.json.IOdbcToJsonEncoder;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIntToJson;
import com.nabla.wapp.server.json.OdbcTimeStampToJson;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.command.AddUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddUserHandler extends AbstractAddHandler<AddUser> {

	private static final List<IOdbcToJsonEncoder>	columns = new LinkedList<IOdbcToJsonEncoder>();

	static {
		columns.add(new OdbcIntToJson("id"));
		columns.add(new OdbcBooleanToJson("active"));
		columns.add(new OdbcTimeStampToJson("created"));
	}

	@Override
	public StringResult execute(final AddUser record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		validate(record, ctx);
		// return more info than just ID
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT id, active, created FROM user WHERE id=?;", add(record, ctx));
		try {
			final JsonResponse json = new JsonResponse();
			json.putNext(stmt.executeQuery(), columns);
			return json.toStringResult();
		} finally {
			Database.close(stmt);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	protected int add(AddUser record, IUserSessionContext ctx) throws DispatchException, SQLException {
		final Integer id = new UserManager(ctx.getWriteConnection()).addUser(record.getName(), record.getPassword());
		if (id == null)
			throw new ValidationException(record.NAME, CommonServerErrors.DUPLICATE_ENTRY);
		return id;
	}

}
