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
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.fixed_asset.UpdateAssetDisposal;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateAssetDisposalHandler extends AbstractUpdateHandler<UpdateAssetDisposal> {

	private static final Log									log = LogFactory.getLog(UpdateAssetDisposalHandler.class);
	private static final UpdateStatement<UpdateAssetDisposal>	sql = new UpdateStatement<UpdateAssetDisposal>(UpdateAssetDisposal.class);

	@Override
	protected void update(final UpdateAssetDisposal record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// validate disposal date: must be at least in following month!
		final Calendar dt = new GregorianCalendar();
		dt.setTime(getAssetAcqisitionDate(ctx.getReadConnection(), record.getId()));
		dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		final Calendar dtDisposal = new GregorianCalendar();
		dtDisposal.setTime(record.getDisposalDate());
		if (!dt.before(dtDisposal))
			throw new ValidationException(IAsset.DISPOSAL_DATE, ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);
		// retrieve current disposal date if any and company ID for this asset
		Date oldDisposalDate;
		Integer companyId;
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT t.disposal_date, c.company_id" +
" FROM fa_asset AS t INNER JOIN fa_company_asset_category AS c ON c.id=t.fa_company_asset_category_id" +
" WHERE t.id=?;", record.getId());
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
			sql.execute(guard.getConnection(), record);
			if (oldDisposalDate != null && record.getDisposalDate() != oldDisposalDate) {
				if (log.isDebugEnabled())
					log.debug("reverting old disposal data");
				RevertAssetDisposalHandler.revertDisposal(guard.getConnection(), record.getId());
			}
			if (oldDisposalDate == null || record.getDisposalDate() != oldDisposalDate) {
				if (log.isDebugEnabled())
					log.debug("disposing asset...");
				dispose(guard.getConnection(), record);
				UserPreference.save(ctx, companyId, IAsset.DISPOSAL_PREFERENCE_GROUP, "disposal_date", record.getDisposalDate());
				UserPreference.save(ctx, companyId, IAsset.DISPOSAL_PREFERENCE_GROUP, "disposal_type", record.getDisposalType());
			}
			guard.setSuccess();
		} finally {
			guard.close();
		}
	}

	private void dispose(final Connection conn, final UpdateAssetDisposal record) throws SQLException, DispatchException {
		final PreparedStatement redo = conn.prepareStatement(
"INSERT INTO fa_transaction_redo (fa_asset_id, command) VALUES(?,?);");
		try {
			redo.setInt(1, record.getId());
			// backup transaction after disposal if any
			if (log.isDebugEnabled())
				log.debug("backing up transactions after disposal date");
			// charge monthly depreciation in disposal month if disposal is after 15
			final Calendar dt = new GregorianCalendar();
			dt.setTime(record.getDisposalDate());
			if (dt.get(GregorianCalendar.DAY_OF_MONTH) >= dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)/2)
				dt.add(GregorianCalendar.MONTH, 1);
			dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
			final Date from = new Date(dt.getTime().getTime());
			// get list of transactions to backup before we delete them
			final IntegerSet transIds = new IntegerSet();
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT t.*" +
" FROM fa_transaction AS t INNER JOIN period_end AS p ON t.period_end_id=p.id" +
" WHERE t.fa_asset_id=? AND p.end_date>?;",
record.getId(), from);
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					while (rs.next()) {
						transIds.add(rs.getInt("id"));
						final String command = MessageFormat.format(
"INSERT INTO fa_transaction" +
" (id,fa_asset_id,period_end_id,amount,class,type,depreciation_period)" +
" VALUES({0,number,0},{1,number,0},{2,number,0},{3,number,0},''{4}'',''{5}'',{6,number,0});",
							rs.getInt("id"),
							rs.getInt("fa_asset_id"),
							rs.getInt("period_end_id"),
							rs.getInt("amount"),
							rs.getString("class"),
							rs.getString("type"),
							Database.getInteger(rs, "depreciation_period"));
						if (log.isTraceEnabled())
							log.trace("redo = " + command);
						redo.setString(2, command);
						redo.addBatch();
					}
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
			// remove any transaction after disposal date
			if (log.isDebugEnabled())
				log.debug("removing transactions after disposal date");
			Database.executeUpdate(conn,
"DELETE FROM fa_transaction WHERE id IN (?);", transIds);
			// add disposal transactions
			if (log.isDebugEnabled())
				log.debug("adding transactions for disposal");
			final TransactionList transactions = new TransactionList(record.getId());
			// closing cost
			transactions.add(new Transaction(TransactionClasses.COST, TransactionTypes.CLOSING, record.getDisposalDate(), -1 * getAssetCost(conn, record.getId())));
			// closing accumulated depreciation
			transactions.add(new Transaction(TransactionClasses.DEP, TransactionTypes.CLOSING, record.getDisposalDate(), -1 * getAssetDepreciation(conn, record.getId())));
			for (Integer newTransId : transactions.save(conn, true)) {
				redo.setString(2,
MessageFormat.format("DELETE FROM fa_transaction WHERE id={0,number,0};", newTransId));
				redo.addBatch();
			}
			if (!Database.isBatchCompleted(redo.executeBatch()))
				throw new InternalErrorException("failed to save disposal transactions");
		} finally {
			redo.close();
		}
	}

	private int getAssetCost(final Connection conn, int assetId) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount) FROM fa_transaction WHERE fa_asset_id=? AND class='COST';", assetId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				return rs.next() ? rs.getInt(1) : 0;
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	private int getAssetDepreciation(final Connection conn, int assetId) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount) FROM fa_transaction WHERE fa_asset_id=? AND class='DEP';", assetId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				return rs.next() ? rs.getInt(1) : 0;
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
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
