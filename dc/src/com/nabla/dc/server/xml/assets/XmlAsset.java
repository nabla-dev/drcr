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
package com.nabla.dc.server.xml.assets;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

@Root
@IRecordTable(name=IAsset.TABLE)
class XmlAsset extends Node implements IAsset {

	public static final String	NAME = "name";
	public static final String	CATEGORY = "category";
	public static final String	REFERENCE = "reference";
	public static final String	LOCATION = "location";
	public static final String	PURCHASE_INVOICE = "purchase_invoice";
	public static final String	ACQUISITION_DATE = "acquisition_date";
	public static final String	ACQUISITION_TYPE = "acquisition_type";
	public static final String	DEPRECIATION_PERIOD = "depreciation_period";
	public static final String	STRAIGHT_LINE_DEPRECIATION = "straight_line_depreciation";

	@Element(name=NAME)
	@IRecordField
	XmlString					name;
	@Element(name=CATEGORY)
	XmlString					category;
	@IRecordField
	Integer						fa_company_asset_category_id;
	@Element(name=REFERENCE,required=false)
	@IRecordField
	XmlString					reference;
	@Element(name=LOCATION,required=false)
	@IRecordField
	XmlString					location;
	@Element(name=PURCHASE_INVOICE,required=false)
	@IRecordField
	XmlString					purchaseInvoice;
	@Element(name=ACQUISITION_DATE)
	@IRecordField
	Date						acquisitionDate;
	@Element(name=ACQUISITION_TYPE)
	@IRecordField
	AcquisitionTypes			acquisitionType;
	@Element(name=DEPRECIATION_PERIOD)
	@IRecordField
	Integer						depreciationPeriod;
	@Element(name=STRAIGHT_LINE_DEPRECIATION,required=false)
	StraightLineDepreciation	depreciation;

	public XmlAsset() {}
/*
	public XmlAsset(final ResultSet rs) throws SQLException {
		code = new XmlString(rs.getString(1));
		name = new XmlString(rs.getString(2));
		visible = rs.getBoolean(3);
		String s = rs.getString(4);
		if (!rs.wasNull())
			cc = new XmlString(s);
		s = rs.getString(5);
		if (!rs.wasNull())
			dep = new XmlString(s);
		bs = rs.getBoolean(6);
	}
*/
	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (name.validate(NAME, NAME_CONSTRAINT, errors) &&
				!ctx.getNameList().add(name.getValue())) {
			errors.add(name.getRow(), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		}
		category.validate(CATEGORY, IFixedAssetCategory.NAME_CONSTRAINT, errors);
		if (reference != null)
			reference.validate(REFERENCE, REFERENCE_CONSTRAINT, errors);
		if (location != null)
			location.validate(LOCATION, LOCATION_CONSTRAINT, errors);
		if (purchaseInvoice != null)
			purchaseInvoice.validate(PURCHASE_INVOICE, PURCHASE_INVOICE_CONSTRAINT, errors);
		if (depreciationPeriod < 1)
			errors.add(getRow(), DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
	}

	public void postValidate(@Nullable final ImportContext.Company company, final IErrorList<Integer> errors) throws DispatchException {
		if (company == null)
			errors.add(category.getRow(), CATEGORY, ServerErrors.UNDEFINED_ASSET_CATEGORY);
		else {
			final ImportContext.Category cat = company.get(category);
			if (cat == null)
				errors.add(category.getRow(), CATEGORY, ServerErrors.UNDEFINED_ASSET_CATEGORY);
			else {
				fa_company_asset_category_id = cat.getId();
				if (cat.getMinDepreciationPeriod() > depreciationPeriod || cat.getMaxDepreciationPeriod() < depreciationPeriod)
					errors.add(getRow(), DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
				if (depreciation != null)
					depreciation.postValidate(this, errors);
			}
		}
	}

	public Integer getDepreciationPeriod() {
		return depreciationPeriod;
	}

	public @Nullable StraightLineDepreciation getStraightLineDepreciation() {
		return depreciation;
	}

	public Date getAcquisitionDate() {
		return acquisitionDate;
	}

}