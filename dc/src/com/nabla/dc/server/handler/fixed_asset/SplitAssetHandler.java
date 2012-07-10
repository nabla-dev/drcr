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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.TransactionClasses;
import com.nabla.fixed_assets.shared.TransactionTypes;
import com.nabla.fixed_assets.shared.command.SplitAsset;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.SimpleString;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class SplitAssetHandler extends AbstractOperationHandler<SplitAsset, SplitAssetHandler.Record> {

	@Root(name="data")
	static class Record {

		@Element Integer	id;
		@Element String		nameA;
		@Element String		nameB;
		@Element(required=false)
		SimpleString		referenceA;
		@Element(required=false)
		SimpleString		referenceB;
		@Element(required=false)
		Integer				cost;
		@Element(required=false)
		Integer				other;
		@Element(required=false)
		Integer				total;

		@Validate
		public void validate() throws DispatchException {
			IAsset.NAME_CONSTRAINT.validate(IAsset.NAME_A, nameA);
			IAsset.NAME_CONSTRAINT.validate(IAsset.NAME_B, nameB);
			if (referenceA != null) {
				if (referenceA.isEmpty())
					referenceA = null;
				else
					IAsset.REFERENCE_CONSTRAINT.validate(IAsset.REFERENCE_A, referenceA);
			}
			if (referenceB != null) {
				if (!referenceB.isEmpty())
					IAsset.REFERENCE_CONSTRAINT.validate(IAsset.REFERENCE_B, referenceB);
			} else
				referenceB = new SimpleString();
			if (getSplitTransaction() &&
				(other == null || total == null || cost < 1 || other < 1 || (cost + other) != total))
					throw new ValidationException(IAsset.COST, CommonServerErrors.INVALID_VALUE);
		}

		public boolean getSplitTransaction() {
			return cost != null;
		}

	}

	public SplitAssetHandler() {
		super(true, IPrivileges.ASSET_EDIT);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final UpdateAssetFieldHandler.Record assetA = new UpdateAssetFieldHandler.Record();
		assetA.id = request.id;
		assetA.name = new SimpleString(request.nameA);
		assetA.reference = request.referenceA;
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			UpdateAssetFieldHandler.sql.execute(conn, assetA);
			final Integer assetBId = Database.addRecord(conn,
"INSERT INTO asset (name,reference,asset_category_id,register_id,location,acquisition_type,pi,dep_period)" +
" SELECT ? AS 'name',? AS 'reference',asset_category_id,register_id,location,acquisition_type,pi,dep_period" +
" FROM asset WHERE id=?;", request.nameB, request.referenceB, request.id);
			if (request.getSplitTransaction()) {
				final PreparedStatement stmtA = conn.prepareStatement(
"UPDATE transaction SET amount=? WHERE id=?;");
				try {
					stmtA.clearBatch();
					final PreparedStatement stmtB = conn.prepareStatement(
"INSERT INTO transaction (asset_id, date, class, type, amount, dep_period) VALUES(?,?,?,?,?,?);");
					try {
						stmtB.clearBatch();
						stmtB.setInt(1, assetBId);
						final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT * FROM transaction WHERE asset_id=?;", request.id);
						try {
							final ResultSet rs = stmt.executeQuery();
							while (rs.next()) {
								stmtA.setInt(2, rs.getInt("id"));

								stmtB.setDate(2, rs.getDate("date"));
								stmtB.setString(3, rs.getString("class"));
								stmtB.setString(4, rs.getString("type"));
								stmtB.setInt(6, rs.getInt("dep_period"));

								final TransactionClasses clazz = TransactionClasses.valueOf(rs.getString("class"));
								final TransactionTypes type = TransactionTypes.valueOf(rs.getString("type"));
								if (clazz == TransactionClasses.COST && type == TransactionTypes.OPENING) {
									// split cost as user inputted
									stmtA.setInt(1, request.cost);
									stmtA.addBatch();
									stmtB.setInt(5, request.other);
									stmtB.addBatch();
								} else {
									// split amount using cost split rate
									int amount = rs.getInt("amount");
									if (amount < 2) {
										stmtB.setInt(5, 0);
										stmtB.addBatch();
									} else {
										int amountA = amount * request.cost / request.total;
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
							try { stmt.close(); } catch (final SQLException __) {}
						}
						if (!Database.isBatchCompleted(stmtB.executeBatch()))
							throw new InternalErrorException("failed to add transactions");
					} finally {
						try { stmtB.close(); } catch (final SQLException __) {}
					}
					if (!Database.isBatchCompleted(stmtA.executeBatch()))
						throw new InternalErrorException("failed to add transactions");
				} finally {
					try { stmtA.close(); } catch (final SQLException __) {}
				}
			}
			guard.setSuccess();
			return serialize(assetBId);
		} finally {
			guard.close();
		}
	}

}
