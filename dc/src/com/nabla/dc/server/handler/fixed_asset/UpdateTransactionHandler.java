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
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.ServerErrors;
import com.nabla.fixed_assets.shared.TransactionClasses;
import com.nabla.fixed_assets.shared.TransactionTypes;
import com.nabla.fixed_assets.shared.command.UpdateTransaction;
import com.nabla.fixed_assets.shared.model.ITransaction;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateTransactionHandler extends AbstractOperationHandler<UpdateTransaction, UpdateTransactionHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="transaction")
	static class Record {

		private final Connection	conn;
		private int					assetId;
		private int					assetDepPeriod;
		private TransactionClasses	trxClass;
		private TransactionTypes	trxType;

		@Element
		@IRecordField(id=true)
		Integer				id;
		@Element(required=false)
		@IRecordField
		Date				date;
		@Element(required=false)
		@IRecordField
		Integer				amount;
		@Element(required=false)
		@IRecordField
		Integer				dep_period;

		public Record(final Connection conn) {
			this.conn = conn;
		}

		@Validate
		public void validate() throws DispatchException, SQLException {
			// check asset has not been disposed
			{
				final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT asset.id, disposal_type, asset.dep_period, class, type FROM asset INNER JOIN transaction ON asset.id=transaction.asset_id WHERE transaction.id=?;", id);
				try {
					final ResultSet rs = stmt.executeQuery();
					if (!rs.next())
						throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
					rs.getString("disposal_type");
					if (!rs.wasNull())
						throw new ActionException(ServerErrors.CANNOT_EDIT_DISPOSED_ASSET);
					assetId = rs.getInt("id");
					assetDepPeriod = rs.getInt("dep_period");
					trxClass = TransactionClasses.valueOf(rs.getString("class"));
					trxType = TransactionTypes.valueOf(rs.getString("type"));
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			}
			// do more checking
			if (date != null) {
				final Calendar dt = new GregorianCalendar();
				dt.setTime(date);
				if (dt.get(Calendar.DAY_OF_MONTH) != dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) &&
					isEndOfMonthRequired())
					throw new ValidationException(ITransaction.DATE, ServerErrors.TRANSACTION_DATE_EOM);
			}
			if (amount != null) {
				if (trxClass == TransactionClasses.COST) {
					switch (trxType) {
					case OPENING:
						if (amount < 0)
							throw new ValidationException(ITransaction.AMOUNT, ServerErrors.TRANSACTION_AMOUNT_MUST_BE_POSITIVE);
						break;
						// CLOSING state cannot be because asset not disposed of
				/*	case CLOSING:
						if (amount > 0)
							throw new ValidationException(ITransaction.AMOUNT, CommonServerErrors.INVALID_VALUE);
						break;*/
					default:
						break;
					}
				} else {
					switch (trxType) {
					case OPENING:
					case CHARGE:
						if (amount > 0)
							throw new ValidationException(ITransaction.AMOUNT, ServerErrors.TRANSACTION_AMOUNT_MUST_BE_NEGATIVE);
						break;
						// CLOSING state cannot be because asset not disposed of
				/*	case CLOSING:
						if (amount > 0)
							throw new ValidationException(ITransaction.AMOUNT, CommonServerErrors.INVALID_VALUE);
						break;*/
					default:
						break;
					}
				}
				// make sure SUM of trans >= 0 i.e. residual value >= 0
				final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount) FROM transaction WHERE asset_id=? AND id!=?;", assetId, id);
				try {
					final ResultSet rs = stmt.executeQuery();
					rs.next();
					int residual_value = rs.getInt(1) + amount;
					if (residual_value < 0)
						throw new ValidationException(ITransaction.AMOUNT, ServerErrors.NBV_MUST_BE_POSITIVE);
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			}
			if (dep_period != null) {
				if (!isDepPeriodRequired())
					// dep period has no meaning in those cases
					throw new ValidationException(ITransaction.DEP_PERIOD, CommonServerErrors.INVALID_VALUE);
				ITransaction.DEP_PERIOD_CONSTRAINT.validate(ITransaction.DEP_PERIOD, dep_period);
				// make sure SUM dep_period <= asset dep period
				final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(dep_period) FROM transaction WHERE asset_id=? AND id!=? AND class='DEP';", assetId, id);
				try {
					final ResultSet rs = stmt.executeQuery();
					rs.next();
					if ((rs.getInt(1) + dep_period) > assetDepPeriod)
						throw new ValidationException(ITransaction.DEP_PERIOD, ServerErrors.INVALID_FINAL_DEP_PERIOD);
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			}
		}

		private boolean isEndOfMonthRequired() {
			return	(trxClass == TransactionClasses.COST && trxType == TransactionTypes.REVALUATION) ||
					(trxClass == TransactionClasses.DEP /*&& trxType != TransactionTypes.CLOSING*/);
		}

		private boolean isDepPeriodRequired() {
			return trxClass == TransactionClasses.DEP /*&& trxType != TransactionTypes.CLOSING*/;
		}
	}

	public static final UpdateStatement<Record>	sql = new UpdateStatement<Record>(Record.class);

	public UpdateTransactionHandler() {
		super(true, IPrivileges.ASSET_EDIT);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		sql.execute(ctx.getWriteConnection(), request);
		return null;
	}

	@Override
	protected Record createRecord(IUserSessionContext ctx) {
		return new Record(ctx.getReadConnection());
	}

}
