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
package com.nabla.dc.server.xml.settings;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
class XmlCompanyAssetCategory extends Node {

	@Attribute
	String		financial_statement_category;
	@Text
	String		asset_category;

	public XmlCompanyAssetCategory() {}

	public XmlCompanyAssetCategory(final ResultSet rs) throws SQLException {
		financial_statement_category = rs.getString(1);
		asset_category = rs.getString(2);
	}

	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (IFixedAssetCategory.NAME_CONSTRAINT.validate(getRow(), "asset_category", asset_category, errors, ValidatorContext.ADD) &&
			!ctx.getNameList().add(asset_category))
			errors.add(getRow(), "asset_category", CommonServerErrors.DUPLICATE_ENTRY);
		IFinancialStatementCategory.NAME_CONSTRAINT.validate(getRow(), "financial_statement_category", financial_statement_category, errors, ValidatorContext.ADD);
	}

	public String getFinancialStatementCategory() {
		return financial_statement_category;
	}

	public String getAssetCategory() {
		return asset_category;
	}
}