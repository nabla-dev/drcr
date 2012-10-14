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
import java.sql.SQLException;
import java.util.List;

import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IInitialDepreciation;
import com.nabla.dc.shared.model.fixed_asset.IOpeningDepreciation;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author nabla
 *
 */
public class AssetDepreciation {

	private final IAssetRecord					asset;
	private final IStraightLineDepreciation	method;
	private final IInitialDepreciation			initial;
	private final IOpeningDepreciation			opening;
	private final Date							openingDate;
	private int								accumulatedDepreciation;
	private int								monthCount;

	public AssetDepreciation(final IAssetRecord record) {
		this.asset = record;
		this.method = asset.getDepreciationMethod();
		this.initial = method.getInitialDepreciation();
		this.opening = method.getOpeningDepreciation();
		if (opening != null)
			openingDate = new Date(opening.getDate().getTime());
		else
			openingDate = null;
	}

	public int getAccumulatedDepreciation() {
		return accumulatedDepreciation;
	}

	public int getMonthCount() {
		return monthCount;
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
		transactions.add(new Transaction(TransactionClasses.COST, TransactionTypes.OPENING, asset.getAcquisitionDate(), method.getCost()));
		// initial accumulated depreciation
//		transactions.add(new Transaction(TransactionClasses.DEP, TransactionTypes.OPENING, asset.getAcquisitionDate(), -1 * asset.getInitialAccumulatedDepreciation(), asset.getInitialDepreciationPeriod()));
	}

	public void getDepreciationTransactions(final List<Transaction> transactions) {
/*		accumulatedDepreciation = asset.getInitialAccumulatedDepreciation();
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
		}*/
	}

}
