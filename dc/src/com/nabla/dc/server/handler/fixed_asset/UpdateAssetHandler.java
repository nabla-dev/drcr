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

import com.nabla.dc.shared.command.fixed_asset.UpdateAsset;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateAssetHandler extends AbstractUpdateHandler<UpdateAsset> {

	public static final UpdateStatement<UpdateAsset>	sql = new UpdateStatement<UpdateAsset>(UpdateAsset.class);

	@Override
	protected void update(final UpdateAsset record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// final validation
		final ValidationException errors = new ValidationException();
		Asset.validateDepreciationPeriod(ctx.getReadConnection(), record, null, errors);
		final IStraightLineDepreciation method = record.getDepreciationMethod();
		if (method != null)
			Asset.validate(method, record.getAcquisitionDate(), null, errors);
		if (!errors.isEmpty())
			throw errors;
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			sql.execute(guard.getConnection(), record);
			final TransactionList transactions = new TransactionList(record.getId());
			transactions.createTransactions(record);
			if (method != null)
				transactions.clearTable(guard.getConnection());
			else
				transactions.clearCost(guard.getConnection());
			transactions.save(guard.getConnection());
			if (method == null && getResidualValue(guard.getConnection(), record.getId()) < 0) {
				errors.add(IAsset.COST, CommonServerErrors.INVALID_VALUE);
				throw errors;
			}
			UserPreference.save(ctx, record.getCompanyId(), IAsset.PREFERENCE_GROUP, IAsset.CATEGORY, record.getCompanyAssetCategoryId());
			UserPreference.save(ctx, record.getCompanyId(), IAsset.PREFERENCE_GROUP, IAsset.LOCATION, record.getLocation());
			if (method != null)
				UserPreference.save(ctx, record.getCompanyId(), IAsset.PREFERENCE_GROUP, IAsset.RESIDUAL_VALUE, method.getResidualValue());
			guard.setSuccess();
		} finally {
			guard.close();
		}
	}

	private int getResidualValue(final Connection conn, int assetId) throws SQLException {
		final PreparedStatement stmt = conn.prepareStatement(
"SELECT SUM(t.amount)" +
" FROM fa_transaction AS t" +
" WHERE t.fa_asset_id=?;");
		try {
			stmt.setInt(1, assetId);
			final ResultSet rs = stmt.executeQuery();
			try {
				rs.next();
				return rs.getInt(1);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}
}
