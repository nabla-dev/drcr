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

import com.nabla.dc.shared.command.fixed_asset.SplitAsset;
import com.nabla.dc.shared.command.fixed_asset.UpdateAssetField;
import com.nabla.dc.shared.model.fixed_asset.ISplitAsset;
import com.nabla.dc.shared.model.fixed_asset.ITransaction;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.NullableString;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
public class SplitAssetHandler extends AbstractAddHandler<SplitAsset> {

	@Override
	protected int add(final SplitAsset record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			final UpdateAssetField assetA = new UpdateAssetField(record.getId(), record.getNameA(), record.getReferenceA());
			// update asset as assetA
			UpdateAssetFieldHandler.sql.execute(guard.getConnection(), assetA);
			// then add assetB
			final Integer assetBId = Database.addRecord(guard.getConnection(),
"INSERT INTO fa_asset (fa_company_asset_category_id,name,reference,location,acquisition_date,acquisition_type,purchase_invoice,depreciation_period)" +
" SELECT fa_company_asset_category_id,? AS 'name',? AS 'reference',location,acquisition_date,acquisition_type,purchase_invoice,depreciation_period" +
" FROM fa_asset WHERE id=?;", record.getNameB(), new NullableString(record.getReferenceB()), record.getId());
			// split transactions
			final PreparedStatement stmtA = guard.getConnection().prepareStatement(
"UPDATE fa_transaction SET amount=? WHERE id=?;");
			try {
				stmtA.clearBatch();
				final PreparedStatement stmtB = guard.getConnection().prepareStatement(
"INSERT INTO fa_transaction (fa_asset_id,period_end_id,class,type,amount,depreciation_period) VALUES(?,?,?,?,?,?);");
				try {
					stmtB.clearBatch();
					stmtB.setInt(1, assetBId);
					final PreparedStatement stmt = StatementFormat.prepare(guard.getConnection(),
"SELECT * FROM fa_transaction WHERE fa_asset_id=?;", record.getId());
					try {
						final ResultSet rs = stmt.executeQuery();
						try {
							while (rs.next()) {
								stmtA.setInt(2, rs.getInt(IdField.NAME));

								stmtB.setInt(2, rs.getInt("period_end_id"));
								stmtB.setString(3, rs.getString(ITransaction.CLASS));
								stmtB.setString(4, rs.getString(ITransaction.TYPE));
								stmtB.setInt(6, rs.getInt(ITransaction.DEPRECIATION_PERIOD));

								final TransactionClasses clazz = TransactionClasses.valueOf(rs.getString(ITransaction.CLASS));
								final TransactionTypes type = TransactionTypes.valueOf(rs.getString(ITransaction.TYPE));
								if (clazz == TransactionClasses.COST && type == TransactionTypes.OPENING) {
									// split cost as user inputted
									stmtA.setInt(1, record.getCostA());
									stmtA.addBatch();
									stmtB.setInt(5, record.getCostB());
									stmtB.addBatch();
								} else {
									// split amount using cost split rate
									int amount = rs.getInt(ITransaction.AMOUNT);
									if (amount < 2) {
										// no change for A
										stmtB.setInt(5, 0);
										stmtB.addBatch();
									} else {
										int amountA = amount * record.getCostA() / record.getTotal();
										if (amountA < 1)
											amountA = 1;
										stmtA.setInt(1, amountA);
										stmtA.addBatch();
										stmtB.setInt(5, amount - amountA);
										stmtB.addBatch();
									}
								}
							}
						} finally {
							rs.close();
						}
					} finally {
						stmt.close();
					}
					if (!Database.isBatchCompleted(stmtB.executeBatch()))
						throw new InternalErrorException("failed to split asset transactions");
				} finally {
					stmtB.close();
				}
				if (!Database.isBatchCompleted(stmtA.executeBatch()))
					throw new InternalErrorException("failed to split asset transactions");
			} finally {
				stmtA.close();
			}
			guard.setSuccess();
			return assetBId;
		} finally {
			guard.close();
		}
	}

	@Override
	protected void generateResponse(final JsonResponse json, @SuppressWarnings("unused") final SplitAsset record, int recordId, @SuppressWarnings("unused") final IUserSessionContext ctx) throws DispatchException, SQLException {
		json.put(ISplitAsset.ID_B, recordId);
	}
}
