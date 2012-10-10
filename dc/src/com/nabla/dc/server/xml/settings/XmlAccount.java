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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

@Root
@IRecordTable(name=IAccount.TABLE)
class XmlAccount extends Node {

	@IRecordField
	Integer		company_id;
	@Element
	@IRecordField
	XmlString	code;
	@Element
	@IRecordField
	XmlString	name;
	@IRecordField
	String		uname;
	@Element(required=false)
	@IRecordField(name="active")
	Boolean		visible;
	@Element(required=false)
	@IRecordField(name="cost_centre")
	XmlString	cc;
	@Element(required=false)
	@IRecordField(name="department")
	XmlString	dep;
	@Element
	@IRecordField(name="balance_sheet")
	Boolean		bs;

	public XmlAccount() {}

	public XmlAccount(final ResultSet rs) throws SQLException {
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

	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (code.validate("code", IAccount.CODE_CONSTRAINT, errors) &&
				!ctx.getAccountCodeList().add(code.getValue())) {
			errors.add(code.getRow(), "code", CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (name.validate("name", IAccount.NAME_CONSTRAINT, errors)) {
			if (ctx.getNameList().add(name.getValue()))
				uname = name.getValue().toUpperCase();
			else
				errors.add(name.getRow(), "name", CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (cc != null) {
			if (cc.isEmpty())
				cc = null;
			else
				cc.validate("cc", IAccount.COST_CENTRE_CONSTRAINT, errors);
		}
		if (dep != null) {
			if (dep.isEmpty())
				dep = null;
			else
				dep.validate("dep", IAccount.DEPARTMENT_CONSTRAINT, errors);
		}
		if (visible == null)
			visible = true;
	}

	public void setCompanyId(final Integer companyId) {
		company_id = companyId;
	}

}