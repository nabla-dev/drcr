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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.server.handler.fixed_asset.Asset;
import com.nabla.dc.server.handler.fixed_asset.TransactionList;
import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IAssetTable;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.wapp.server.database.InsertStatement;
import com.nabla.wapp.server.xml.IRowMap;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IAssetTable.TABLE)
class XmlAsset extends Node implements IAssetRecord {

	static final String	NAME = "name";
	static final String	CATEGORY = "category";
	static final String	REFERENCE = "reference";
	static final String	LOCATION = "location";
	static final String	PURCHASE_INVOICE = "purchase_invoice";
	static final String	ACQUISITION_DATE = "acquisition_date";
	static final String	ACQUISITION_TYPE = "acquisition_type";
	static final String	DEPRECIATION_PERIOD = "depreciation_period";
	static final String	COST = "cost";
	static final String	STRAIGHT_LINE_DEPRECIATION = "straight_line_depreciation";
	static final String	DISPOSAL = "disposal";

	public static final InsertStatement<XmlAsset>	sql = new InsertStatement<XmlAsset>(XmlAsset.class);

	@Attribute
	Integer			xmlRow;
	@Attribute
	Integer			xmlRowMapId;
	@Element(name=NAME)
	@IRecordField(name=IAssetTable.NAME)
	String					name;
	@Element(name=CATEGORY)
	String					category;
	@IRecordField(name=IAssetTable.CATEGORY_ID)
	Integer						fa_company_asset_category_id;
	@Element(name=REFERENCE,required=false)
	@IRecordField(name=IAssetTable.REFERENCE)
	String					reference;
	@Element(name=LOCATION,required=false)
	@IRecordField(name=IAssetTable.LOCATION)
	String					location;
	@Element(name=PURCHASE_INVOICE,required=false)
	@IRecordField(name=IAssetTable.PURCHASE_INVOICE)
	String					purchaseInvoice;
	@Element(name=ACQUISITION_DATE)
	@IRecordField(name=IAssetTable.ACQUISITION_DATE)
	Date						acquisitionDate;
	@Element(name=ACQUISITION_TYPE)
	@IRecordField(name=IAssetTable.ACQUISITION_TYPE)
	AcquisitionTypes			acquisitionType;
	@Element(name=DEPRECIATION_PERIOD)
	@IRecordField(name=IAssetTable.DEPRECIATION_PERIOD)
	Integer						depreciationPeriod;
	@Element(name=COST)
	Integer						cost;
	@Element(name=STRAIGHT_LINE_DEPRECIATION,required=false)
	XmlStraightLineDepreciation	depreciationMethod;
	@Element(name=DISPOSAL,required=false)
	XmlDisposal					disposal;

	@IRecordField(name=IAssetTable.DISPOSAL_DATE)
	Date						disposalDate;
	@IRecordField(name=IAssetTable.DISPOSAL_TYPE)
	DisposalTypes				disposalType;
	@IRecordField(name=IAssetTable.PROCEEDS)
	Integer						proceeds;

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
	public Integer getRow() {
		return xmlRow;
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();
		final IRowMap rows = ctx.getRowMap(xmlRowMapId);

		if (IAsset.NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.ADD) &&
				!ctx.getNameList().add(name)) {
			errors.add(rows.get(NAME), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		}
		IFixedAssetCategory.NAME_CONSTRAINT.validate(CATEGORY, category, errors, ValidatorContext.ADD);
		IAsset.REFERENCE_CONSTRAINT.validate(REFERENCE, reference, errors, ValidatorContext.UPDATE);
		IAsset.LOCATION_CONSTRAINT.validate(LOCATION, location, errors, ValidatorContext.UPDATE);
		IAsset.PURCHASE_INVOICE_CONSTRAINT.validate(PURCHASE_INVOICE, purchaseInvoice, errors, ValidatorContext.UPDATE);
		Validator.execute(this, getRow(), errors);
	}

	public void postValidate(@Nullable final Company company, final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();
		final IRowMap rows = ctx.getRowMap(xmlRowMapId);

		if (company == null)
			errors.add(rows.get(CATEGORY), CATEGORY, ServerErrors.UNDEFINED_ASSET_CATEGORY);
		else {
			final Category cat = company.get(category);
			if (cat == null)
				errors.add(rows.get(CATEGORY), CATEGORY, ServerErrors.UNDEFINED_ASSET_CATEGORY);
			else {
				fa_company_asset_category_id = cat.getId();
				if (cat.getMinDepreciationPeriod() > depreciationPeriod || cat.getMaxDepreciationPeriod() < depreciationPeriod)
					errors.add(rows.get(DEPRECIATION_PERIOD), getDepreciationPeriodField(), CommonServerErrors.INVALID_VALUE);
				if (depreciationMethod != null)
					depreciationMethod.postValidate(this, errors);
				if (disposal != null) {
					disposal.postValidate(acquisitionDate, errors);
					disposalDate = disposal.getDate();
					disposalType = disposal.getType();
					proceeds = disposal.getProceeds();
				}
			}
		}
	}

	public boolean save(final Connection conn, @SuppressWarnings("unused") final SaveContext ctx) throws SQLException, DispatchException {
		// final validation
		int assetId = sql.execute(conn, this);
		final TransactionList transactions = new TransactionList(assetId);
		transactions.createTransactions(this);
		transactions.save(conn);
		if (disposal != null)
			Asset.dispose(conn, assetId, disposal);
		return true;
	}

	@Override
	public Integer getDepreciationPeriod() {
		return depreciationPeriod;
	}

	@Override
	public Date getAcquisitionDate() {
		return acquisitionDate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getCost() {
		return cost;
	}

	@Override
	public @Nullable IStraightLineDepreciation getDepreciationMethod() {
		return depreciationMethod;
	}

	@Override
	public @Nullable IDisposal getDisposal() {
		return disposal;
	}

	@Override
	public Integer getTotalDepreciation() {
		return (depreciationMethod == null) ? cost : (cost - depreciationMethod.getResidualValue());
	}

	@Override
	public String getCostField() {
		return COST;
	}

	@Override
	public Integer getCompanyAssetCategoryId() {
		return fa_company_asset_category_id;
	}

	@Override
	public String getCategoryField() {
		return CATEGORY;
	}

	@Override
	public String getDepreciationPeriodField() {
		return DEPRECIATION_PERIOD;
	}


}