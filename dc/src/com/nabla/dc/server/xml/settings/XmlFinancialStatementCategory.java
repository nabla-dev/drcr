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

import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.wapp.server.xml.IRowMap;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IFinancialStatementCategory.TABLE)
class XmlFinancialStatementCategory extends Node {

	static final String	NAME = "name";

	@Attribute
	Integer		xmlRowMapId;
	@Element(name=NAME)
	@IRecordField
	String		name;
	@IRecordField
	String		uname;
	@Element(name="visible", required=false)
	@IRecordField
	Boolean		active;

	public XmlFinancialStatementCategory() {}

	public XmlFinancialStatementCategory(final ResultSet rs) throws SQLException {
		name = rs.getString(1);
		active = rs.getBoolean(2);
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();
		final IRowMap rows = ctx.getRowMap(xmlRowMapId);

		if (IFinancialStatementCategory.NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.ADD)) {
			if (ctx.getNameList().add(name))
				uname = name.toUpperCase();
			else
				errors.add(rows.get(NAME), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (active == null)
			active = false;
	}

}