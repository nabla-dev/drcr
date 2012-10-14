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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.dc.shared.command.fixed_asset.UpdateAssetDisposal;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateAssetDisposalHandler extends AbstractUpdateHandler<UpdateAssetDisposal> {

	private static final Log									log = LogFactory.getLog(UpdateAssetDisposalHandler.class);
	private static final UpdateStatement<UpdateAssetDisposal>	sql = new UpdateStatement<UpdateAssetDisposal>(UpdateAssetDisposal.class);

	@Override
	protected void update(final UpdateAssetDisposal cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ValidationException errors = new ValidationException();
		if (!Asset.validate(cmd, getAssetAcqisitionDate(ctx.getReadConnection(), cmd.getId()), null, errors))
			throw errors;
		// retrieve current disposal date if any and company ID for this asset
		Date oldDisposalDate;
		Integer companyId;
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT t.disposal_date, c.company_id" +
" FROM fa_asset AS t INNER JOIN fa_company_asset_category AS c ON c.id=t.fa_company_asset_category_id" +
" WHERE t.id=?;", cmd.getId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
				oldDisposalDate = rs.getDate(1);
				companyId = rs.getInt(2);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			// update asset record
			sql.execute(guard.getConnection(), cmd);
			if (oldDisposalDate != null && cmd.getDate() != oldDisposalDate) {
				if (log.isDebugEnabled())
					log.debug("reverting old disposal data");
				RevertAssetDisposalHandler.revertDisposal(guard.getConnection(), cmd.getId());
			}
			if (oldDisposalDate == null || cmd.getDate() != oldDisposalDate) {
				if (log.isDebugEnabled())
					log.debug("disposing asset...");
				Asset.dispose(guard.getConnection(), cmd.getId(), cmd);
				UserPreference.save(ctx, companyId, IAsset.DISPOSAL_PREFERENCE_GROUP, "disposal_date", cmd.getDate());
				UserPreference.save(ctx, companyId, IAsset.DISPOSAL_PREFERENCE_GROUP, "disposal_type", cmd.getType());
			}
			guard.setSuccess();
		} finally {
			guard.close();
		}
	}

	private Date getAssetAcqisitionDate(final Connection conn, int assetId) throws SQLException, ActionException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT acquisition_date FROM fa_asset WHERE id=?;", assetId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
				return rs.getDate(1);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}
}
