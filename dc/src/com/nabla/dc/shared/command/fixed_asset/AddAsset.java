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
package com.nabla.dc.shared.command.fixed_asset;

import java.sql.Date;

import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAsset.TABLE)
public class AddAsset implements IRecordAction<StringResult>, IAsset {

	@IRecordField
	Integer				company_id;
	@IRecordField
	Integer				fa_asset_category_id;
	@IRecordField
	String				name;
	@IRecordField
	String				reference;
	@IRecordField
	String				location;
	Date				acquisition_date;
	@IRecordField
	AcquisitionTypes	acquisition_type;
	Integer				cost;
	@IRecordField
	String				purchase_invoice;
	Integer				initial_accum_depreciation;
	Integer				initial_depreciation_period;
	@IRecordField
	Integer				depreciation_period;
	Integer				residual_value;
	Boolean				createTransactions;
	Boolean				opening;
	Integer				opening_year;
	Integer				opening_month;	// 0-based
	Integer				opening_accum_depreciation;
	Integer				opening_depreciation_period;

	AddAsset() {}	// for serialization only

	public AddAsset(final Integer companyId, final String code, final String name, final String cc, final String dep, final Boolean bs, final Boolean active) {
		this.company_id = companyId;
		this.code = code;
		this.name = name;
		this.cost_centre = cc;
		this.department = dep;
		this.balance_sheet = bs;
		this.active = active;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		IAsset.NAME_CONSTRAINT.validate(IAsset.NAME, name);
		IAsset.REFERENCE_CONSTRAINT.validate(IAsset.REFERENCE, reference);
		IAsset.LOCATION_CONSTRAINT.validate(IAsset.LOCATION, location);

		if (acquisition_type == AcquisitionTypes.TRANSFER) {
			if (initial_accum_dep == null)
				initial_dep_period = null;
			else if (initial_dep_period == null)
				throw new ValidationException(IAsset.INITIAL_DEP_PERIOD, CommonServerErrors.REQUIRED_VALUE);
		} else {
			initial_accum_dep = null;
			initial_dep_period = null;
		}
		IAsset.PI_CONSTRAINT.validate(IAsset.PI, pi);

		if (opening) {
			if (opening_year == null)
				throw new ValidationException(IAsset.OPENING_YEAR, CommonServerErrors.REQUIRED_VALUE);
			if (opening_month == null)
				throw new ValidationException(IAsset.OPENING_MONTH, CommonServerErrors.REQUIRED_VALUE);
			final Calendar dtOpening = new GregorianCalendar();
			dtOpening.setLenient(false);
			try {
				dtOpening.set(opening_year, opening_month, 1);
			} catch (Exception __) {
				throw new ValidationException(IAsset.OPENING_MONTH, CommonServerErrors.INVALID_VALUE);
			}
			if (opening_accum_dep == null)
				throw new ValidationException(IAsset.OPENING_ACCUM_DEP, CommonServerErrors.REQUIRED_VALUE);
			if (opening_dep_period == null)
				throw new ValidationException(IAsset.OPENING_DEP_PERIOD, CommonServerErrors.REQUIRED_VALUE);
			if (opening_dep_period < 1)
				throw new ValidationException(IAsset.OPENING_DEP_PERIOD, CommonServerErrors.INVALID_VALUE);
		} else {
			opening_year = null;
			opening_month = null;
			opening_accum_dep = null;
			opening_dep_period = null;
		}

		if (createTransactions) {
			if (acquisition_date == null)
				throw new ValidationException(IAsset.ACQUISITION_DATE, CommonServerErrors.REQUIRED_VALUE);
			if (cost == null)
				throw new ValidationException(IAsset.COST, CommonServerErrors.REQUIRED_VALUE);
			if (residual_value == null)
				residual_value = 0;
			else if (residual_value < 0)
				throw new ValidationException(IAsset.COST, CommonServerErrors.INVALID_VALUE);
		}
		return false;
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getAcquisitionDate() {
		return acquisition_date;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public Integer getInitialAccumulatedDepreciation() {
		return initial_accum_dep;
	}

	@Override
	public Integer getInitialDepreciationPeriod() {
		return initial_dep_period;
	}

	@Override
	public Integer getOpeningYear() {
		return opening_year;
	}

	@Override
	public Integer getOpeningMonth() {
		return opening_month;
	}

	@Override
	public Integer getOpeningAccumulatedDepreciation() {
		return opening_accum_dep;
	}

	@Override
	public int getDepreciationPeriod() {
		return dep_period;
	}

	@Override
	public int getResidualValue() {
		return residual_value;
	}

	@Override
	public Integer getAssetCategoryId() {
		return asset_category_id;
	}

	@Override
	public Integer getOpeningDepreciationPeriod() {
		return opening_dep_period;
	}

	@Override
	public Date getDisposalDate() {
		return null;
	}

}
