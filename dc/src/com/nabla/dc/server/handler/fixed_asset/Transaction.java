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

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.nabla.dc.shared.model.fixed_asset.ITransaction;
import com.nabla.dc.shared.model.fixed_asset.TransactionClasses;
import com.nabla.dc.shared.model.fixed_asset.TransactionTypes;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.general.Nullable;

/**
 * @author nabla64
 *
 */
@IRecordTable(name=ITransaction.TABLE)
public class Transaction {

	@IRecordField
	int							fa_asset_id;
	@IRecordField(name="class")
	final TransactionClasses	clazz;
	@IRecordField
	final TransactionTypes		type;
	final Date					date;
	@IRecordField
	int							period_end_id;
	@IRecordField
	final int					amount;
	@IRecordField
	final Integer				depreciation_period;

	public Transaction(final TransactionClasses clazz, final TransactionTypes type, final Calendar date, int value, @Nullable final Integer depreciationPeriod) {
		Assert.argument((clazz == TransactionClasses.COST && depreciationPeriod == null) || (clazz != TransactionClasses.COST && depreciationPeriod != null));
		this.clazz = clazz;
		this.type = type;
		date.set(GregorianCalendar.DAY_OF_MONTH, date.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		this.date = Util.calendarToSqlDate(date);
		this.amount = value;
		this.depreciation_period = depreciationPeriod;
	}

	public Transaction(final TransactionClasses clazz, final TransactionTypes type, final Calendar date, int value) {
		this(clazz, type, date, value, (clazz == TransactionClasses.COST) ? null : 1);
	}

	public Transaction(final Calendar date, int value, @Nullable final Integer depreciationPeriod) {
		this(TransactionClasses.DEP, TransactionTypes.CHARGE, date, value, depreciationPeriod);
	}

	public Transaction(final Calendar date, int value) {
		this(TransactionClasses.DEP, TransactionTypes.CHARGE, date, value);
	}

	public Transaction(final TransactionClasses clazz, final TransactionTypes type, final java.util.Date date, int value, @Nullable final Integer depreciationPeriod) {
		this(clazz, type, Util.dateToCalendar(date), value, depreciationPeriod);
	}

	public Transaction(final TransactionClasses clazz, final TransactionTypes type, final java.util.Date date, int value) {
		this(clazz, type, Util.dateToCalendar(date), value);
	}

	public Transaction(final java.util.Date date, int value) {
		this(Util.dateToCalendar(date), value);
	}

	public Date getPeriodEndDate() {
		return date;
	}

	public void setAssetId(int id) {
		fa_asset_id = id;
	}

	public void setPeriodEndId(int id) {
		period_end_id = id;
	}

}
