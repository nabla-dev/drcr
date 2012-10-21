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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.nabla.dc.shared.model.company.IFinancialYear;
import com.nabla.dc.shared.model.company.IPeriodEnd;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IInitialDepreciation;
import com.nabla.dc.shared.model.fixed_asset.IOpeningDepreciation;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author FNorais
 *
 */
public class TransactionList extends LinkedList<Transaction> {

	private static final long serialVersionUID = 1L;

	private final int	assetId;

	public TransactionList(final int assetId) {
		this.assetId= assetId;
	}

	public void clearTable(final Connection conn) throws SQLException {
		Database.executeUpdate(conn, "DELETE FROM fa_transaction WHERE fa_asset_id=?;", assetId);
	}

	public void createTransactions(final IAssetRecord asset) throws DispatchException {
		// opening cost
		add(new Transaction(TransactionClasses.COST, TransactionTypes.OPENING, asset.getAcquisitionDate(), asset.getCost()));
		int accumulatedDepreciation = 0;
		int monthCount = 0;
		final IInitialDepreciation init = asset.getInitialDepreciation();
		if (init != null) {
			// initial accumulated depreciation
			add(new Transaction(TransactionClasses.DEP, TransactionTypes.OPENING, asset.getAcquisitionDate(), -1 * init.getValue(), init.getPeriodCount()));
			accumulatedDepreciation = init.getValue();
			monthCount = init.getPeriodCount();
		}
		if (accumulatedDepreciation >= asset.getTotalDepreciation())
			return;	// nothing to do
		final Calendar dt = new GregorianCalendar();
		final IOpeningDepreciation opening = asset.getOpeningDepreciation();
		if (opening != null) {
			dt.setTime(opening.getDate());
			dt.add(GregorianCalendar.DATE, -1);
			add(new Transaction(dt, -1 * (opening.getValue() - accumulatedDepreciation), opening.getPeriodCount() - monthCount));
			accumulatedDepreciation = opening.getValue();
			monthCount = opening.getPeriodCount();
			if (accumulatedDepreciation >= asset.getTotalDepreciation())
				return;	// nothing more to do than the opening
		} else {
			dt.setTime(asset.getAcquisitionDate());
			// charge monthly depreciation from same month if acquisition date is before 15 otherwise following month
			if (dt.get(GregorianCalendar.DAY_OF_MONTH) < dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)/2)
				dt.add(GregorianCalendar.MONTH, -1);
		}
		final IStraightLineDepreciation method = asset.getDepreciationMethod();
		dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
		int monthly_depreciation = (asset.getCost() - accumulatedDepreciation - method.getResidualValue()) / (asset.getDepreciationPeriod() - monthCount);
		int rem_depreciation = asset.getCost() - accumulatedDepreciation - method.getResidualValue() - monthly_depreciation * (asset.getDepreciationPeriod() - monthCount);
		while (monthCount < asset.getDepreciationPeriod()) {
			++monthCount;
			dt.add(GregorianCalendar.MONTH, 1);
			if (monthly_depreciation == 0 && rem_depreciation == 0)
				continue;
			int depreciation = monthly_depreciation;
			if (rem_depreciation > 0) {
				++depreciation;
				--rem_depreciation;
			}
			add(new Transaction(dt, depreciation * -1));
			accumulatedDepreciation += depreciation;
		}
	}

	public List<Integer> save(final Connection conn) throws SQLException, DispatchException {
		return save(conn, false);
	}

	public List<Integer> save(final Connection conn, boolean autoGenerateKeys) throws SQLException, DispatchException {
		int companyId = getCompanyId(conn);
		confirmPeriodEnd(conn, companyId);
		// convert period end date to id
		final PreparedStatement stmt = conn.prepareStatement(
"SELECT p.id" +
" FROM period_end AS p INNER JOIN financial_year AS y ON p.financial_year_id=y.id" +
" WHERE y.company_id=? AND p.end_date=?;");
		try {
			stmt.setInt(1, companyId);
			for (Transaction t : this) {
				t.setAssetId(assetId);
				stmt.setDate(2, t.getPeriodEndDate());
				final ResultSet rs = stmt.executeQuery();
				try {
					rs.next();
					t.setPeriodEndId(rs.getInt(1));
				} finally {
					rs.close();
				}
			}
		} finally {
			stmt.close();
		}
		final BatchInsertStatement<Transaction> batch = new BatchInsertStatement<Transaction>(conn, Transaction.class, autoGenerateKeys);
		try {
			batch.add(this);
			return batch.execute();
		} finally {
			batch.close();
		}
	}

	private void confirmPeriodEnd(final Connection conn, int companyId) throws SQLException, InternalErrorException {
		final Calendar first = new GregorianCalendar();
		first.setTime(getFirst().getPeriodEndDate());
		final Calendar last = new GregorianCalendar();
		last.setTime(getLast().getPeriodEndDate());
		// find first period end date defined for company
		final Calendar dt = new GregorianCalendar();
		{
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT p.end_date" +
" FROM period_end AS p INNER JOIN financial_year AS y ON p.financial_year_id=y.id" +
" WHERE y.company_id=?" +
" ORDER BY p.end_date ASC LIMIT 1;", companyId);
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next())
						Util.throwInternalErrorException("company definition has not been finalised");
					dt.setTime(rs.getDate(1));
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		}
		if (first.before(dt)) {
			final SimpleDateFormat financialYearNameFormat = new SimpleDateFormat(IFinancialYear.NAME_FORMAT);
			final SimpleDateFormat periodEndNameFormat = new SimpleDateFormat(IPeriodEnd.NAME_FORMAT);
			dt.add(GregorianCalendar.MONTH, -1);
			dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
			final PreparedStatement stmt = conn.prepareStatement(
"INSERT IGNORE INTO period_end (financial_year_id,name,end_date) VALUES(?,?,?);");
			try {
				do {
					// ok we are in trouble because this is before time began!
					// so assume 12 month year
					final Integer financialYearId = Database.addUniqueRecord(conn,
"INSERT INTO financial_year (company_id, name) VALUES(?,?);",
companyId, financialYearNameFormat.format(new Date(dt.getTime().getTime())));
					if (financialYearId == null)
						Util.throwInternalErrorException("cannot create previous financial year: name duplicate");
					stmt.setInt(1, financialYearId);
					for (int m = 0; m < 12; ++m) {
						final Date end = new Date(dt.getTime().getTime());
						stmt.setString(2, periodEndNameFormat.format(end));
						stmt.setDate(3, end);
						stmt.addBatch();
						dt.add(GregorianCalendar.MONTH, -1);
						dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
					}
				} while (first.before(dt));
				if (!Database.isBatchCompleted(stmt.executeBatch()))
					Util.throwInternalErrorException("fail to add period ends");
			} finally {
				stmt.close();
			}
		}
		// find last period end date defined for company
		Integer financialYearId;
		{
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT p.end_date, p.financial_year_id" +
" FROM period_end AS p INNER JOIN financial_year AS y ON p.financial_year_id=y.id" +
" WHERE y.company_id=?" +
" ORDER BY p.end_date DESC LIMIT 1;", companyId);
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next())
						Util.throwInternalErrorException("company definition has not been finalised");
					dt.setTime(rs.getDate(1));
					financialYearId = rs.getInt(2);
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		}
		if (dt.before(last)) {
			final PreparedStatement stmt = conn.prepareStatement(
"INSERT IGNORE INTO period_end (financial_year_id,name,end_date) VALUES(?,?,?);");
			try {
				stmt.setInt(1, financialYearId);
				final SimpleDateFormat periodEndNameFormat = new SimpleDateFormat(IPeriodEnd.NAME_FORMAT);
				do {
					dt.add(GregorianCalendar.MONTH, 1);
					dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
					final Date end = new Date(dt.getTime().getTime());
					stmt.setString(2, periodEndNameFormat.format(end));
					stmt.setDate(3, end);
					stmt.addBatch();
				} while (dt.before(last));
				if (!Database.isBatchCompleted(stmt.executeBatch()))
					Util.throwInternalErrorException("fail to add period ends");
			} finally {
				stmt.close();
			}
		}
	}

	private int getCompanyId(final Connection conn) throws SQLException, InternalErrorException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT t.company_id" +
" FROM fa_asset AS a INNER JOIN fa_company_asset_category AS t ON a.fa_company_asset_category_id=t.id" +
" WHERE a.id=?;", assetId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					Util.throwInternalErrorException("asset has been removed");
				return rs.getInt(1);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}
}
