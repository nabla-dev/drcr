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
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.dc.shared.model.fixed_asset.IOpeningDepreciation;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
public abstract class Asset {

	private static final Log		log = LogFactory.getLog(Asset.class);
/*
	static public <P> boolean validate(final IErrorList<P> errors) {

		return false;
	}
*/
	static public <P> boolean validateDepreciationPeriod(final Connection conn, final IAssetRecord asset, @Nullable final P pos, final IErrorList<P> errors) throws SQLException, DispatchException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT t.min_depreciation_period, t.max_depreciation_period" +
" FROM fa_asset_category AS t INNER JOIN fa_company_asset_category AS r ON r.fa_asset_category_id=t.id" +
" WHERE r.id=?;", asset.getCompanyAssetCategoryId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next()) {
					errors.add(pos, asset.getCategoryField(), ServerErrors.UNDEFINED_ASSET_CATEGORY_FOR_COMPANY);
					return false;
				}
				if (rs.getInt("min_depreciation_period") > asset.getDepreciationPeriod() || rs.getInt("max_depreciation_period") < asset.getDepreciationPeriod()) {
					errors.add(pos, asset.getDepreciationPeriodField(), CommonServerErrors.INVALID_VALUE);
					return false;
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		return true;
	}

	static public <P> boolean validate(final IOpeningDepreciation opening, final java.util.Date dtAcquisition, @Nullable final P pos, final IErrorList<P> errors) throws DispatchException {
		if (Util.dateToCalendar(dtAcquisition).before(Util.dateToCalendar(opening.getDate())))
			return true;
		errors.add(pos, opening.getDateField(), ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);
		return false;
	}

	static public <P> boolean validate(final IDisposal disposal, final java.util.Date dtAcquisition, @Nullable final P pos, final IErrorList<P> errors) throws DispatchException {
		// validate disposal date: must be at least in following month!
		final Calendar dtDisposal = Util.dateToCalendar(disposal.getDate());
		dtDisposal.set(GregorianCalendar.DAY_OF_MONTH, 1);
		if (Util.dateToCalendar(dtAcquisition).before(dtDisposal))
			return true;
		errors.add(pos, disposal.getDateField(), ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);
		return false;
	}

	static public void dispose(final Connection conn, final Integer assetId, final IDisposal disposal) throws SQLException, DispatchException {
		final PreparedStatement redo = conn.prepareStatement(
"INSERT INTO fa_transaction_redo (fa_asset_id, command) VALUES(?,?);");
		try {
			redo.setInt(1, assetId);
			// backup transaction after disposal if any
			if (log.isDebugEnabled())
				log.debug("backing up transactions after disposal date");
			// charge monthly depreciation in disposal month if disposal is after 15
			final Calendar dt = Util.dateToCalendar(disposal.getDate());
			if (dt.get(GregorianCalendar.DAY_OF_MONTH) >= dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)/2)
				dt.add(GregorianCalendar.MONTH, 1);
			dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
			final Date from = Util.calendarToSqlDate(dt);
			// get list of transactions to backup before we delete them
			final IntegerSet transIds = new IntegerSet();
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT t.*" +
" FROM fa_transaction AS t INNER JOIN period_end AS p ON t.period_end_id=p.id" +
" WHERE t.fa_asset_id=? AND p.end_date>?;", assetId, from);
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
			final TransactionList transactions = new TransactionList(assetId);
			// closing cost
			transactions.add(new Transaction(TransactionClasses.COST, TransactionTypes.CLOSING, disposal.getDate(), -1 * getAssetCostBeforeDisposal(conn, assetId)));
			// closing accumulated depreciation
			transactions.add(new Transaction(TransactionClasses.DEP, TransactionTypes.CLOSING, disposal.getDate(), -1 * getAssetDepreciationBeforeDisposal(conn, assetId)));
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

	static private int getAssetCostBeforeDisposal(final Connection conn, int assetId) throws SQLException {
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

	static private int getAssetDepreciationBeforeDisposal(final Connection conn, int assetId) throws SQLException {
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

}
