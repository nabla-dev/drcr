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
package com.nabla.dc.server.handler.fixed_asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import com.nabla.wapp.shared.dispatch.DispatchException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.UpdateBalanceSheetCategoryDefinition;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.model.AbstractOperationHandler;

/**
 * @author nabla
 *
 */
public class UpdateBalanceSheetCategoryDefinitionHandler extends AbstractOperationHandler<UpdateBalanceSheetCategoryDefinition, UpdateBalanceSheetCategoryDefinitionHandler.Request> {

	@Root(name="data")
	public static class Request {
		@Element
		public Integer			id;
		@ElementList(entry="category",required=false, inline=true)
		public Set<Integer>		categories;
	}

	public UpdateBalanceSheetCategoryDefinitionHandler() {
		super(true, IPrivileges.BS_CATEGORY_EDIT);
	}

	@Override
	protected String execute(final Request request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			Database.executeUpdate(conn,
"DELETE FROM bs_category_definition WHERE bs_category_id=?;", request.id);
			final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO bs_category_definition (bs_category_id, asset_category_id) VALUES(?,?);");
			try {
				stmt.clearBatch();
				stmt.setInt(1, request.id);
				for (final Integer childId : request.categories) {
					stmt.setInt(2, childId);
					stmt.addBatch();
				}
				guard.setSuccess(Database.isBatchCompleted(stmt.executeBatch()));
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		} finally {
			guard.close();
		}
		return null;
	}

}
