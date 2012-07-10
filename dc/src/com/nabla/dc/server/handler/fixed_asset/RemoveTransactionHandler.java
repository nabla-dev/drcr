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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.fixed_asset.RemoveTransaction;
import com.nabla.dc.shared.model.fixed_asset.ITransaction;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.model.AbstractRemoveHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.VoidResult;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author nabla
 *
 */
public class RemoveTransactionHandler extends AbstractRemoveHandler<RemoveTransaction> {

	public RemoveTransactionHandler() {
		super(ITransaction.TABLE);
	}

	@Override
	public VoidResult execute(final RemoveTransaction cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (!cmd.isEmpty()) {
			// check if asset has not been disposed of
			final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT disposal_type" +
" FROM fa_asset AS a INNER JOIN fa_transaction AS t ON a.id=t.ta_asset_id" +
" WHERE t.id=?;", cmd.iterator().next());
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next())
						throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
					rs.getString("disposal_type");
					if (!rs.wasNull())
						throw new ActionException(ServerErrors.CANNOT_EDIT_DISPOSED_ASSET);
				} finally {
					Database.close(rs);
				}
			} finally {
				Database.close(stmt);
			}
			return super.execute(cmd, ctx);
		}
		return null;
	}
}
