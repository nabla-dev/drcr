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

import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.server.xml.IRowMap;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IAccount.TABLE)
class XmlAccount extends Node {

	static final String	CODE = "code";
	static final String	NAME = "name";
	static final String	CC = "cc";
	static final String	DEP = "dep";

	@Attribute
	Integer		xmlRowMapId;
	@IRecordField
	Integer		company_id;
	@Element(name=CODE)
	@IRecordField
	String	code;
	@Element(name=NAME)
	@IRecordField
	String	name;
	@IRecordField
	String		uname;
	@Element(required=false)
	@IRecordField(name="active")
	Boolean		visible;
	@Element(name=CC,required=false)
	@IRecordField(name="cost_centre")
	String	cc;
	@Element(name=DEP,required=false)
	@IRecordField(name="department")
	String	dep;
	@Element
	@IRecordField(name="balance_sheet")
	Boolean		bs;

	public XmlAccount() {}

	public XmlAccount(final ResultSet rs) throws SQLException {
		code = rs.getString(1);
		name = rs.getString(2);
		visible = rs.getBoolean(3);
		String s = rs.getString(4);
		if (!rs.wasNull())
			cc = s;
		s = rs.getString(5);
		if (!rs.wasNull())
			dep = s;
		bs = rs.getBoolean(6);
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();
		final IRowMap rows = ctx.getRowMap(xmlRowMapId);

		if (IAccount.CODE_CONSTRAINT.validate(CODE, code, errors, ValidatorContext.ADD) &&
				!ctx.getAccountCodeList().add(code)) {
			errors.add(rows.get(CODE), CODE, CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (IAccount.NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.ADD)) {
			if (ctx.getNameList().add(name))
				uname = name.toUpperCase();
			else
				errors.add(rows.get(NAME), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (cc != null) {
			if (cc.isEmpty())
				cc = null;
			else
				IAccount.COST_CENTRE_CONSTRAINT.validate(CC, cc, errors, ValidatorContext.ADD);
		}
		if (dep != null) {
			if (dep.isEmpty())
				dep = null;
			else
				IAccount.DEPARTMENT_CONSTRAINT.validate(DEP, dep, errors, ValidatorContext.ADD);
		}
		if (visible == null)
			visible = true;
	}

	public void setCompanyId(final Integer companyId) {
		company_id = companyId;
	}

}