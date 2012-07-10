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
import com.nabla.fixed_assets.shared.command.AddTransaction;
import com.nabla.fixed_assets.shared.model.ITransaction;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.InsertStatement;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddTransactionHandler extends AbstractOperationHandler<AddTransaction, AddTransactionHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="transaction")
	static class Record {

		private final Connection	conn;
		private int					assetDepPeriod;

		@Element
		@IRecordField(name="asset_id")
		Integer				recordId;
		@Element(name="class")
		@IRecordField(name="class")
		TransactionClasses	trxClass;
		@Element(name="type")
		@IRecordField(name="type")
		TransactionTypes	trxType;
		@Element
		@IRecordField
		Date				date;
		@Element
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
"SELECT disposal_type, dep_period FROM asset WHERE id=?;", recordId);
				try {
					final ResultSet rs = stmt.executeQuery();
					if (!rs.next())
						throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
					rs.getString("disposal_type");
					if (!rs.wasNull())
						throw new ActionException(ServerErrors.CANNOT_EDIT_DISPOSED_ASSET);
					assetDepPeriod = rs.getInt("dep_period");
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			}
			// do more checking
			final Calendar dt = new GregorianCalendar();
			dt.setTime(date);
			int eom = dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			if (dt.get(Calendar.DAY_OF_MONTH) != eom && isEndOfMonthRequired())
				// expecting end of month for date so amend date
				dt.set(GregorianCalendar.DAY_OF_MONTH, eom);
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
				// make sure SUM of trans >= 0 i.e. residual value >= 0
				final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount) FROM transaction WHERE asset_id=?;", recordId);
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
"SELECT SUM(dep_period) FROM transaction WHERE asset_id=? AND class='DEP';", recordId);
				try {
					final ResultSet rs = stmt.executeQuery();
					rs.next();
					if ((rs.getInt(1) + dep_period) > assetDepPeriod)
						throw new ValidationException(ITransaction.DEP_PERIOD, ServerErrors.INVALID_FINAL_DEP_PERIOD);
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			} else if (isDepPeriodRequired()) {
				throw new ValidationException(ITransaction.DEP_PERIOD, CommonServerErrors.REQUIRED_VALUE);
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

	public static final InsertStatement<Record>	sql = new InsertStatement<Record>(Record.class);

	public AddTransactionHandler() {
		super(true, IPrivileges.ASSET_EDIT);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return serialize(sql.execute(ctx.getWriteConnection(), request));
	}

	@Override
	protected Record createRecord(IUserSessionContext ctx) {
		return new Record(ctx.getReadConnection());
	}

}
