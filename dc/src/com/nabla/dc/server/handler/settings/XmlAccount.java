package com.nabla.dc.server.handler.settings;

import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
@IRecordTable(name=IAccount.TABLE)
class XmlAccount {

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

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = XmlNode.getErrorList(session);
		final ImportContext ctx = XmlNode.getContext(session);
		if (IAccount.CODE_CONSTRAINT.validate("code", code.getValue(), errors) &&
				!ctx.getAccountCodeList().add(code.getValue())) {
			errors.setLine(code.getRow());
			errors.add("code", CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (IAccount.NAME_CONSTRAINT.validate("name", name.getValue(), errors)) {
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
				IAccount.CC_CONSTRAINT.validate("cc", cc.getValue(), errors);
		}
		if (dep != null) {
			if (dep.isEmpty())
				dep = null;
			else
				IAccount.DEP_CONSTRAINT.validate("dep", dep.getValue(), errors);
		}
		if (visible == null)
			visible = true;
	}

	public void setCompanyId(final Integer companyId) {
		company_id = companyId;
	}

}