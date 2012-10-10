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
import com.nabla.wapp.shared.model.IErrorList;

@Root
@IRecordTable(name=IAsset.TABLE)
class XmlAsset extends Node implements IAsset {

	Integer						companyId;
	@Element
	@IRecordField
	XmlString					name;
	@Element
	XmlString					category;
	@IRecordField
	Integer						fa_company_asset_category_id;
	@Element(required=false)
	@IRecordField
	XmlString					reference;
	@Element(required=false)
	@IRecordField
	XmlString					location;
	@Element(required=false)
	@IRecordField
	XmlString					purchase_invoice;
	@Element
	@IRecordField
	Date						acquisition_date;
	@Element
	@IRecordField
	AcquisitionTypes			acquisition_type;
	@Element
	@IRecordField
	Integer						depreciation_period;
	@Element(required=false)
	StraightLineDepreciation	straight_line_depreciation;

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
		if (name.validate("name", NAME_CONSTRAINT, errors) &&
				!ctx.getNameList().add(name.getValue())) {
			errors.add(name.getRow(), "name", CommonServerErrors.DUPLICATE_ENTRY);
		}
		category.validate("category", IFixedAssetCategory.NAME_CONSTRAINT, errors);
		if (reference != null)
			reference.validate("reference", REFERENCE_CONSTRAINT, errors);
		if (location != null)
			location.validate("location", LOCATION_CONSTRAINT, errors);
		if (purchase_invoice != null)
			purchase_invoice.validate("purchase_invoice", PURCHASE_INVOICE_CONSTRAINT, errors);

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
			if (openingAccumulatedDepreciation == null)
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, CommonServerErrors.REQUIRED_VALUE);
			else if (openingAccumulatedDepreciation <= getInitialAccumulatedDepreciation())
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION);
			else if (openingAccumulatedDepreciation > (cost - residualValue))
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

			if (openingDepreciationPeriod == null)
				errors.add(OPENING_DEPRECIATION_PERIOD, CommonServerErrors.REQUIRED_VALUE);
			else if (openingDepreciationPeriod < 1)
				errors.add(OPENING_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
			else if (openingDepreciationPeriod > depreciation_period)
				errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
			else if (openingDepreciationPeriod <= getInitialDepreciationPeriod())
				errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
			else if (openingAccumulatedDepreciation != null) {
				if (openingDepreciationPeriod == depreciation_period) {
					if (openingAccumulatedDepreciation < (cost - residualValue))
						errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
				} if (openingAccumulatedDepreciation == (cost - residualValue))
					errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
			}
		}

	}

	public void setCompanyId(final Integer companyId) {
		company_id = companyId;
	}

}