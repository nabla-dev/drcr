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
package com.nabla.wapp.server.model;

import java.sql.SQLException;
import java.text.MessageFormat;

import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.VoidResult;
import com.nabla.wapp.shared.general.AlwaysNull;

/**
 * @author nabla
 *
 */
public abstract class AbstractRemoveHandler<A extends AbstractRemove> extends AbstractHandler<A, VoidResult> {

	private final String	sql;

	protected AbstractRemoveHandler(final String table) {
		super(true);
		sql = MessageFormat.format("DELETE FROM {0} WHERE id IN(?);", table);
	}

	@Override
	public @AlwaysNull VoidResult execute(final A cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (!cmd.isEmpty())
			Database.executeUpdate(ctx.getWriteConnection(), sql, cmd);
		return null;
	}

}
