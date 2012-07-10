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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.RevertAssetDisposal;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.VoidResult;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author nabla
 *
 */
public class RevertAssetDisposalHandler extends AbstractHandler<RevertAssetDisposal, VoidResult> {

	public RevertAssetDisposalHandler() {
		super(true, IPrivileges.ASSET_EDIT);
	}

	@Override
	public VoidResult execute(final RevertAssetDisposal request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			if (!Database.executeUpdate(conn,
"UPDATE asset SET disposal_type=NULL, proceeds=NULL WHERE id=?;", request.getId()))
				throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT command FROM redo WHERE asset_id=?;", request.getId());
			try {
				final Statement redo = conn.createStatement();
				try {
					final ResultSet rs = stmt.executeQuery();
					while (rs.next())
						redo.execute(rs.getString(1));
				} finally {
					try { redo.close(); } catch (final SQLException e) {}
				}
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
			Database.executeUpdate(conn, "DELETE FROM redo WHERE asset_id=?;", request.getId());

			guard.setSuccess();
		} finally {
			guard.close();
		}
		return null;
	}

}
