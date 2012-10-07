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
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IFixedAssetCategory.TABLE)
class XmlAssetCategory {
	@Element
	@IRecordField
	XmlString				name;
	@IRecordField
	String					uname;
	@Element(name="visible",required=false)
	@IRecordField
	Boolean					active;
	@Element(required=false)
	@IRecordField
	FixedAssetCategoryTypes	type;
	@Element
	@IRecordField
	Integer					min_depreciation_period;
	@Element(required=false)
	@IRecordField
	Integer					max_depreciation_period;

	public XmlAssetCategory() {}

	public XmlAssetCategory(final ResultSet rs) throws SQLException {
		name = new XmlString(rs.getString(1));
		active = rs.getBoolean(2);
		type = FixedAssetCategoryTypes.valueOf(rs.getString(3));
		min_depreciation_period = rs.getInt(4);
		max_depreciation_period = rs.getInt(5);
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = XmlNode.getErrorList(session);
		errors.setLine(name.getRow());
		final String n = name.getValue();
		if (IFixedAssetCategory.NAME_CONSTRAINT.validate("name", n, errors, ValidatorContext.ADD)) {
			if (XmlNode.<ImportContext>getContext(session).getNameList().add(n))
				uname = n.toUpperCase();
			else
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
		}
		IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.validate("min_depreciation_period", min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors, ValidatorContext.ADD);
		if (max_depreciation_period == null)
			max_depreciation_period = min_depreciation_period;
		else if (IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.validate("max_depreciation_period", max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors, ValidatorContext.ADD) &&
			max_depreciation_period < min_depreciation_period)
			errors.add("max_depreciation_period", ServerErrors.INVALID_MAX_DEPRECIATION_PERIOD);
		if (active == null)
			active = false;
		if (type == null)
			type = FixedAssetCategoryTypes.TANGIBLE;
	}

}