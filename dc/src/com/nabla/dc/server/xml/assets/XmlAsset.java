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
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.server.xml.settings.ImportContext;
import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IAsset.TABLE)
class XmlAsset {

	Integer				companyId;
	@Element
	@IRecordField
	XmlString			name;
	@Element
	XmlString			category;
	@IRecordField
	Integer				fa_company_asset_category_id;
	@Element(required=false)
	@IRecordField
	String				reference;
	@IRecordField
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
	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = XmlNode.getErrorList(session);
		final ImportContext ctx = XmlNode.getContext(session);
		if (IAccount.CODE_CONSTRAINT.validate("code", code.getValue(), errors, ValidatorContext.ADD) &&
				!ctx.getAccountCodeList().add(code.getValue())) {
			errors.setLine(code.getRow());
			errors.add("code", CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (IAccount.NAME_CONSTRAINT.validate("name", name.getValue(), errors, ValidatorContext.ADD)) {
			if (ctx.getNameList().add(name.getValue()))
				uname = name.getValue().toUpperCase();
			else {
				errors.setLine(name.getRow());
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
			}
		}
		if (cc != null) {
			if (cc.isEmpty())
				cc = null;
			else
				IAccount.COST_CENTRE_CONSTRAINT.validate("cc", cc.getValue(), errors, ValidatorContext.ADD);
		}
		if (dep != null) {
			if (dep.isEmpty())
				dep = null;
			else
				IAccount.DEPARTMENT_CONSTRAINT.validate("dep", dep.getValue(), errors, ValidatorContext.ADD);
		}
		if (visible == null)
			visible = true;
	}

	public void setCompanyId(final Integer companyId) {
		company_id = companyId;
	}

}