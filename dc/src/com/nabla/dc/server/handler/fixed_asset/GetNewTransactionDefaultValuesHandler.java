/**
* Copyright 2011 nabla
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.ServerErrors;
import com.nabla.fixed_assets.shared.TransactionClasses;
import com.nabla.fixed_assets.shared.command.GetNewTransactionDefaultValues;
import com.nabla.fixed_assets.shared.model.ITransaction;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.model.XmlResponseWriter;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * The <code>AbstractGetDefaultValuesHandler</code> object is used to
 *
 */
public class GetNewTransactionDefaultValuesHandler  extends AbstractHandler<GetNewTransactionDefaultValues, StringResult> {

	public GetNewTransactionDefaultValuesHandler() {
		super(false, IPrivileges.ASSET_EDIT);
	}

	@Override
	public StringResult execute(final GetNewTransactionDefaultValues cmd, IUserSessionContext ctx) throws DispatchException, SQLException {
		final Connection conn = ctx.getReadConnection();
		// check if asset has not been disposed of
		int assetDepPeriod;
		PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT disposal_type, dep_period FROM asset WHERE id=?;", cmd.getAssetId());
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
		final XmlResponseWriter response = new XmlResponseWriter();
		response.beginData();
		response.beginRecord();
		response.write(ITransaction.CLASS, cmd.getTransactionClass());
		response.write(ITransaction.TYPE, cmd.getTransactionType());
	//	response.write(ITransaction.ASSET_ID, cmd.getAssetId());
		switch (cmd.getTransactionType()) {
		case OPENING:
			// can only be 1 OPENING for transaction type
			stmt = StatementFormat.prepare(conn,
"SELECT id FROM transaction WHERE asset_id=? AND class=? AND type='OPENING';",
					cmd.getAssetId(), cmd.getTransactionClass());
			try {
				final ResultSet rs = stmt.executeQuery();
				if (rs.next())
					throw new ActionException(ServerErrors.ONLY_ONE_OPENING_TRANSACTION);
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
			//$FALL-THROUGH$
		case REVALUATION: {
			// date = today
			// amount = 1
			// dep_period=n/a
			Date dt;
			if (cmd.getTransactionClass() == TransactionClasses.DEP) {
				// try to see if there is an associated cost
				stmt = StatementFormat.prepare(conn,
"SELECT date FROM transaction WHERE asset_id=? AND class='COST' AND type=? ORDER BY date DESC LIMIT 1;", cmd.getAssetId(), cmd.getTransactionType());
				try {
					final ResultSet rs = stmt.executeQuery();
					if (rs.next())
						dt = rs.getDate(1);
					else
						dt = new GregorianCalendar().getTime();
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			} else
				dt = new GregorianCalendar().getTime();
			response.write(ITransaction.DATE, dt);
			response.write(ITransaction.AMOUNT, cmd.getTransactionClass() == TransactionClasses.COST ? 1 : -1);
			break;
		}
		case CHARGE: {
			if (cmd.getTransactionClass() == TransactionClasses.COST)
				throw new InternalErrorException("a depreciation charge cannot be of COST class");
			response.write(ITransaction.DEP_PERIOD, 1);
			stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount), SUM(dep_period) FROM transaction WHERE asset_id=?;", cmd.getAssetId());
			try {
				final ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					if (rs.getInt(1) == 0 || assetDepPeriod <= rs.getInt(2))
						throw new ActionException(ServerErrors.ASSET_FULLY_DEPRECIATED);
					int depreciation = rs.getInt(1) / (assetDepPeriod - rs.getInt(2));
					if (depreciation == 0)
						depreciation = 1;
					response.write(ITransaction.AMOUNT, -1 * depreciation);
				} else
					response.write(ITransaction.AMOUNT, -1);
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
			final Calendar dt = new GregorianCalendar();
			stmt = StatementFormat.prepare(conn,
"SELECT date FROM transaction WHERE asset_id=? ORDER BY date DESC LIMIT 1;", cmd.getAssetId());
			try {
				final ResultSet rs = stmt.executeQuery();
				if (rs.next())
					dt.setTime(rs.getDate(1));
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
			dt.add(GregorianCalendar.MONTH, 1);
			dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));			// charge monthly depreciation from same month if acquisition date is before 15 otherwise following month
			response.write(ITransaction.DATE, dt.getTime());
			break;
		}
		case CLOSING:
			throw new InternalErrorException("CLOSING type not allowed");
		}
		response.endRecord();
		response.endData();
		return new StringResult(response.toString());
	}

	protected static StringResult executeQuery(final PreparedStatement stmt) throws SQLException {
		try {
			final ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				return null;
			final XmlResponseWriter response = new XmlResponseWriter();
			response.beginData();
			response.beginRecord();
			do {
				response.write(rs.getString("field"), rs.getString("default"));
			} while (rs.next());
			response.endRecord();
			response.endData();
			return new StringResult(response.toString());
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

}
