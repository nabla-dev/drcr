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
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.xml.IRowMap;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IFixedAssetCategory.TABLE)
class XmlAssetCategory extends Node {

	static final String	NAME = "name";
	static final String MIN_DEPRECIATION_PERIOD = "min_depreciation_period";
	static final String MAX_DEPRECIATION_PERIOD = "max_depreciation_period";

	@Attribute
	Integer					xmlRowMapId;
	@Element(name=NAME)
	@IRecordField
	String					name;
	@IRecordField
	String					uname;
	@Element(name="visible", required=false)
	@IRecordField
	Boolean					active;
	@Element(required=false)
	@IRecordField
	FixedAssetCategoryTypes	type;
	@Element(name=MIN_DEPRECIATION_PERIOD)
	@IRecordField
	Integer					min_depreciation_period;
	@Element(name=MAX_DEPRECIATION_PERIOD,required=false)
	@IRecordField
	Integer					max_depreciation_period;

	public XmlAssetCategory() {}

	public XmlAssetCategory(final ResultSet rs) throws SQLException {
		name = rs.getString(1);
		active = rs.getBoolean(2);
		type = FixedAssetCategoryTypes.valueOf(rs.getString(3));
		min_depreciation_period = rs.getInt(4);
		max_depreciation_period = rs.getInt(5);
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();
		final IRowMap rows = ctx.getRowMap(xmlRowMapId);

		if (IFixedAssetCategory.NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.ADD)) {
			if (ctx.getNameList().add(name))
				uname = name.toUpperCase();
			else
				errors.add(rows.get(NAME), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		}
		IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.validate(rows.get(MIN_DEPRECIATION_PERIOD), MIN_DEPRECIATION_PERIOD, min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors, ValidatorContext.ADD);
		if (max_depreciation_period == null)
			max_depreciation_period = min_depreciation_period;
		else if (IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.validate(rows.get(MAX_DEPRECIATION_PERIOD), MAX_DEPRECIATION_PERIOD, max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors, ValidatorContext.ADD) &&
			max_depreciation_period < min_depreciation_period)
			errors.add(rows.get(MAX_DEPRECIATION_PERIOD), MAX_DEPRECIATION_PERIOD, ServerErrors.INVALID_MAX_DEPRECIATION_PERIOD);
		if (active == null)
			active = false;
		if (type == null)
			type = FixedAssetCategoryTypes.TANGIBLE;
	}

}