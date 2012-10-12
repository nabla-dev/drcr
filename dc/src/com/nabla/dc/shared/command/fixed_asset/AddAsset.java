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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IAssetTable;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.dc.shared.model.fixed_asset.IInitialDepreciation;
import com.nabla.dc.shared.model.fixed_asset.IOpeningDepreciation;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.AlwaysNull;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAssetTable.TABLE)
public class AddAsset implements IRecordAction<StringResult>, IAsset, IAssetRecord {

	Integer				companyId;
	@IRecordField
	String				name;
	@IRecordField
	Integer				fa_company_asset_category_id;
	@IRecordField @Nullable
	String				reference;
	@IRecordField @Nullable
	String				location;
	@IRecordField
	Date				acquisition_date;
	@IRecordField
	AcquisitionTypes	acquisition_type;
	int					cost;
	@IRecordField @Nullable
	String				purchase_invoice;
	Integer				initialAccumulatedDepreciation;	// if TRANSFER
	Integer				initialDepreciationPeriod;	// if TRANSFER
	@IRecordField
	int					depreciation_period;
	int					residualValue;
	boolean				createTransactions;
	boolean				opening = false;	// to agree NBV at given period
	Integer				openingYear;
	Integer				openingMonth;	// 0-based
	Integer				openingAccumulatedDepreciation;
	Integer				openingDepreciationPeriod;

	AddAsset() {}	// for serialization only

	public AddAsset(final Integer companyId, final String name, final Integer companyAssetCategoryId,
			@Nullable final String reference, @Nullable final String location,
			final Date acquisitionDate, final AcquisitionTypes acquisitionType, int cost, @Nullable final String pi,
			int depreciationPeriod, final Integer residualValue,
			boolean createTransactions) {
		this.companyId = companyId;
		this.name = name;
		this.fa_company_asset_category_id = companyAssetCategoryId;
		this.reference = reference;
		this.location = location;
		this.acquisition_date = acquisitionDate;
		this.acquisition_type = acquisitionType;
		this.cost = cost;
		this.purchase_invoice = pi;
		this.depreciation_period = depreciationPeriod;
		this.residualValue = (residualValue == null) ? 0 : residualValue;
		this.createTransactions = createTransactions;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.ADD);
	}

	protected boolean doValidate(final IErrorList<Void> errors, final ValidatorContext ctx) throws DispatchException {
		int n = errors.size();

		NAME_CONSTRAINT.validate(NAME, name, errors, ctx);
		REFERENCE_CONSTRAINT.validate(REFERENCE, reference, errors, ctx);
		LOCATION_CONSTRAINT.validate(LOCATION, location, errors, ctx);
		PURCHASE_INVOICE_CONSTRAINT.validate(PURCHASE_INVOICE, purchase_invoice, errors, ctx);

		if (cost < 0)
			errors.add(COST, CommonServerErrors.INVALID_VALUE);
		if (residualValue < 0)
			errors.add(RESIDUAL_VALUE, CommonServerErrors.INVALID_VALUE);
		else if (residualValue > cost)
			errors.add(RESIDUAL_VALUE, ServerErrors.INVALID_RESIDUAL_VALUE);

		if (acquisition_type == AcquisitionTypes.TRANSFER) {
			if (initialAccumulatedDepreciation == null) {
				if (initialDepreciationPeriod != null)
					errors.add(INITIAL_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
			} else {
				if (initialAccumulatedDepreciation < 0)
					errors.add(INITIAL_ACCUMULATED_DEPRECIATION, CommonServerErrors.INVALID_VALUE);
				else if (initialAccumulatedDepreciation > (cost - residualValue))
					errors.add(INITIAL_ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

				if (initialDepreciationPeriod == null)
					errors.add(INITIAL_DEPRECIATION_PERIOD, CommonServerErrors.REQUIRED_VALUE);
				else if (initialDepreciationPeriod < 0)
					errors.add(INITIAL_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
				else if (initialDepreciationPeriod > depreciation_period)
					errors.add(INITIAL_DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
				else if (initialDepreciationPeriod == depreciation_period) {
					if (initialAccumulatedDepreciation < (cost - residualValue))
						errors.add(INITIAL_DEPRECIATION_PERIOD, ServerErrors.INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
				} if (initialAccumulatedDepreciation == (cost - residualValue))
					errors.add(INITIAL_DEPRECIATION_PERIOD, ServerErrors.INITIAL_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD);
			}
		}

		if (opening) {
			if (openingYear == null)
				errors.add(OPENING_YEAR, CommonServerErrors.REQUIRED_VALUE);
			if (openingMonth == null)
				errors.add(OPENING_MONTH, CommonServerErrors.REQUIRED_VALUE);
			else if (openingMonth < 0 || openingMonth > 11)
				errors.add(OPENING_MONTH, CommonServerErrors.INVALID_VALUE);
			int initialDepreciation = (initialAccumulatedDepreciation == null) ? 0 : initialAccumulatedDepreciation;
			if (openingAccumulatedDepreciation == null)
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, CommonServerErrors.REQUIRED_VALUE);
			else if (openingAccumulatedDepreciation <= initialDepreciation)
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION);
			else if (openingAccumulatedDepreciation > (cost - residualValue))
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

			int initialPeriod = (initialDepreciationPeriod == null) ? 0 : initialDepreciationPeriod;
			if (openingDepreciationPeriod == null)
				errors.add(OPENING_DEPRECIATION_PERIOD, CommonServerErrors.REQUIRED_VALUE);
			else if (openingDepreciationPeriod < 1)
				errors.add(OPENING_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
			else if (openingDepreciationPeriod > depreciation_period)
				errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
			else if (openingDepreciationPeriod <= initialPeriod)
				errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
			else if (openingAccumulatedDepreciation != null) {
				if (openingDepreciationPeriod == depreciation_period) {
					if (openingAccumulatedDepreciation < (cost - residualValue))
						errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
				} if (openingAccumulatedDepreciation == (cost - residualValue))
					errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
			}
		}

		return n == errors.size();
	}

	public void setInitialDepreciation(final Integer value, final Integer period) {
		this.initialAccumulatedDepreciation = value;
		this.initialDepreciationPeriod = period;
	}

	public void setOpeningDepreciation(final Integer year, final Integer month, final Integer value, final Integer period) {
		this.opening = true;
		this.openingYear = year;
		this.openingMonth = month;
		this.openingAccumulatedDepreciation = value;
		this.openingDepreciationPeriod = period;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public boolean isCreateTransactions() {
		return createTransactions;
	}

	public boolean isOpeningDepreciation() {
		return opening;
	}

	public String getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getCompanyAssetCategoryId() {
		return fa_company_asset_category_id;
	}

	@Override
	public Date getAcquisitionDate() {
		return acquisition_date;
	}

	@Override
	public int getDepreciationPeriod() {
		return depreciation_period;
	}

	@Override
	@Nullable
	public IStraightLineDepreciation getDepreciation() {
		return createTransactions ?
				new IStraightLineDepreciation() {
			@Override
			public Integer getCost() {
				return cost;
			}

			@Override
			public @Nullable IInitialDepreciation getInitialDepreciation() {

				return (initialAccumulatedDepreciation == null) ?
						null
						:
						new IInitialDepreciation() {
							@Override
							public Integer getValue() {
								return initialAccumulatedDepreciation;
							}

							@Override
							public Integer getPeriodCount() {
								return initialDepreciationPeriod;
							}
				};
			}

			@Override
			public @Nullable IOpeningDepreciation getOpeningDepreciation() {
				return opening ?
					new IOpeningDepreciation() {
						@Override
						public Calendar getDate() {
							final Calendar dt = new GregorianCalendar();
							dt.set(openingYear, openingMonth, 1);
							return dt;
						}

						@Override
						public Integer getValue() {
							return openingAccumulatedDepreciation;
						}

						@Override
						public Integer getPeriodCount() {
							return openingDepreciationPeriod;
						}
				}
					: null;
			}

			@Override
			public Integer getResidualValue() {
				return residualValue;
			}

		}
			: null;
	}

	@Override
	public @AlwaysNull IDisposal getDisposal() {
		return null;
	}

}
