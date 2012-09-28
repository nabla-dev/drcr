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
import java.util.List;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AssetDepreciation {

	private final IAssetRecord		asset;
	private final Date				openingDate;
	private int					accumulatedDepreciation;
	private int					monthCount;

	public AssetDepreciation(final IAssetRecord record) {
		this.asset = record;
		if (record.getOpeningYear() != null) {
			final Calendar dt = new GregorianCalendar();
			dt.set(record.getOpeningYear(), record.getOpeningMonth(), 1);
			openingDate = new Date(dt.getTime().getTime());
		} else
			openingDate = null;
	}

	public int getAccumulatedDepreciation() {
		return accumulatedDepreciation;
	}

	public int getMonthCount() {
		return monthCount;
	}

	public void validateDepreciationPeriod(final Connection conn) throws ValidationException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT t.min_depreciation_period, t.max_depreciation_period" +
" FROM fa_asset_category AS t INNER JOIN fa_company_asset_category AS r ON r.fa_asset_category_id=t.id" +
" WHERE r.id=?;", asset.getCompanyAssetCategoryId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new ValidationException(IAsset.CATEGORY, ServerErrors.UNDEFINED_ASSET_CATEGORY_FOR_COMPANY);
				if (rs.getInt("min_depreciation_period") > asset.getDepreciationPeriod() || rs.getInt("max_depreciation_period") < asset.getDepreciationPeriod())
					throw new ValidationException(IAsset.DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	public void validate() throws ValidationException {
		if (openingDate != null) {
			final Calendar dtOpening = new GregorianCalendar();
			dtOpening.setTime(openingDate);
			final Calendar dtAcquisition = new GregorianCalendar();
			dtAcquisition.setTime(asset.getAcquisitionDate());
			if (!dtAcquisition.before(dtOpening))
				throw new ValidationException(IAsset.OPENING_MONTH, ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);
		}
/*
		if (asset.getDisposalDate() != null) {
			// validate disposal date: must be at least in following month!
			final Calendar dt = new GregorianCalendar();
			dt.setTime(asset.getAcquisitionDate());
			dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
			final Calendar dtDisposal = new GregorianCalendar();
			dtDisposal.setTime(asset.getDisposalDate());
			if (!dt.before(dtDisposal))
				throw new ValidationException(IAsset.DISPOSAL_DATE, ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);
			if (openingDate != null) {
				dt.setTime(openingDate);
				dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
				if (dtDisposal.before(dt))
					throw new ValidationException(IAsset.DISPOSAL_DATE, ServerErrors.MUST_BE_AFTER_OPENING_DATE);
			}
		}*/
	}

	public void clearTransaction(final Connection conn, int  assetId) throws SQLException {
		Database.executeUpdate(conn, "DELETE FROM transaction WHERE fa_asset_id=?;", assetId);
	}

	public void createTransactions(final Connection conn, int assetId) throws SQLException, DispatchException {
		final TransactionList transactions = new TransactionList(assetId);
		getAcquisitionTransactions(transactions);
		getDepreciationTransactions(transactions);
		transactions.save(conn);
	}

	public void getAcquisitionTransactions(final List<Transaction> transactions) {
		// opening cost
		transactions.add(new Transaction(TransactionClasses.COST, TransactionTypes.OPENING, asset.getAcquisitionDate(), asset.getCost()));
		// initial accumulated depreciation
		transactions.add(new Transaction(TransactionClasses.DEP, TransactionTypes.OPENING, asset.getAcquisitionDate(), -1 * asset.getInitialAccumulatedDepreciation(), asset.getInitialDepreciationPeriod()));
	}

	public void getDepreciationTransactions(final List<Transaction> transactions) {
		accumulatedDepreciation = asset.getInitialAccumulatedDepreciation();
		monthCount = asset.getInitialDepreciationPeriod();
		if (accumulatedDepreciation >= (asset.getCost() - asset.getResidualValue()))
			return;	// nothing to do
		final Calendar dt = new GregorianCalendar();
		if (openingDate != null) {
			dt.setTime(openingDate);
			dt.add(GregorianCalendar.DATE, -1);
			transactions.add(new Transaction(dt, -1 * (asset.getOpeningAccumulatedDepreciation() - accumulatedDepreciation), asset.getOpeningDepreciationPeriod() - monthCount));
			if (accumulatedDepreciation >= (asset.getCost() - asset.getResidualValue()))
				return;	// nothing more to do than the opening
		} else {
			dt.setTime(asset.getAcquisitionDate());
			// charge monthly depreciation from same month if acquisition date is before 15 otherwise following month
			if (dt.get(GregorianCalendar.DAY_OF_MONTH) < dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)/2)
				dt.add(GregorianCalendar.MONTH, -1);
		}
		dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
		int monthly_depreciation = (asset.getCost() - accumulatedDepreciation - asset.getResidualValue()) / (asset.getDepreciationPeriod() - monthCount);
		int rem_depreciation = asset.getCost() - accumulatedDepreciation - asset.getResidualValue() - monthly_depreciation * (asset.getDepreciationPeriod() - monthCount);
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
			transactions.add(new Transaction(dt, depreciation * -1));
			accumulatedDepreciation += depreciation;
		}
	}

}
