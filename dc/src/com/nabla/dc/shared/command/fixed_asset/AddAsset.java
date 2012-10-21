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

import java.util.Date;

import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IAssetTable;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.dc.shared.model.fixed_asset.StraightlineDepreciation;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.AlwaysNull;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAssetTable.TABLE)
public class AddAsset implements IRecordAction<StringResult>, IAssetRecord {

	Integer								companyId;
	@IRecordField(name=IAssetTable.NAME)
	String								name;
	@IRecordField(name=IAssetTable.CATEGORY_ID)
	Integer								fa_company_asset_category_id;
	@IRecordField(name=IAssetTable.REFERENCE)
	@Nullable String					reference;
	@IRecordField(name=IAssetTable.LOCATION)
	@Nullable String					location;
	@IRecordField(name=IAssetTable.ACQUISITION_DATE)
	Date								acquisition_date;
	@IRecordField(name=IAssetTable.ACQUISITION_TYPE)
	AcquisitionTypes					acquisition_type;
	@IRecordField(name=IAssetTable.PURCHASE_INVOICE)
	@Nullable String					purchase_invoice;
	@IRecordField(name=IAssetTable.DEPRECIATION_PERIOD)
	Integer								depreciation_period;
	Integer								cost;
	@Nullable StraightlineDepreciation	depreciationMethod;

	AddAsset() {}	// for serialization only

	public AddAsset(final Integer companyId, final String name, final Integer companyAssetCategoryId,
			@Nullable final String reference, @Nullable final String location,
			final Date acquisitionDate, final AcquisitionTypes acquisitionType, int cost, @Nullable final String pi,
			int depreciationPeriod) {
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
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.ADD);
	}

	protected boolean doValidate(final IErrorList<Void> errors, final ValidatorContext ctx) throws DispatchException {
		int n = errors.size();

		IAsset.NAME_CONSTRAINT.validate(IAsset.NAME, name, errors, ctx);
		IAsset.REFERENCE_CONSTRAINT.validate(IAsset.REFERENCE, reference, errors, ctx);
		IAsset.LOCATION_CONSTRAINT.validate(IAsset.LOCATION, location, errors, ctx);
		IAsset.PURCHASE_INVOICE_CONSTRAINT.validate(IAsset.PURCHASE_INVOICE, purchase_invoice, errors, ctx);
		Validator.execute(this, null, errors);
		if (depreciationMethod != null)
			depreciationMethod.validate(this, errors);

		return n == errors.size();
	}

	public void setDepreciationMethod(final StraightlineDepreciation method) {
		this.depreciationMethod = method;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public String getLocation() {
		return location;
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
	public Integer getDepreciationPeriod() {
		return depreciation_period;
	}

	@Override
	public @Nullable IStraightLineDepreciation getDepreciationMethod() {
		return depreciationMethod;
	}

	@Override
	public @AlwaysNull IDisposal getDisposal() {
		return null;
	}

	@Override
	public Integer getCost() {
		return cost;
	}

	@Override
	public Integer getTotalDepreciation() {
		return (depreciationMethod == null) ? cost : cost - depreciationMethod.getResidualValue();
	}

	@Override
	public String getCostField() {
		return IAsset.COST;
	}

	@Override
	public Integer getCompanyAssetCategoryId() {
		return fa_company_asset_category_id;
	}

	@Override
	public String getCategoryField() {
		return IAsset.CATEGORY;
	}

	@Override
	public String getDepreciationPeriodField() {
		return IAsset.DEPRECIATION_PERIOD;
	}

}
