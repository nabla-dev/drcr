package com.nabla.dc.server.xml.settings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
@IRecordTable(name=IFinancialStatementCategory.TABLE)
class XmlFinancialStatementCategory {
	@Element
	@IRecordField
	XmlString	name;
	@IRecordField
	String		uname;
	@Element(name="visible", required=false)
	@IRecordField
	Boolean		active;

	public XmlFinancialStatementCategory() {}

	public XmlFinancialStatementCategory(final ResultSet rs) throws SQLException {
		name = new XmlString(rs.getString(1));
		active = rs.getBoolean(2);
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = XmlNode.getErrorList(session);
		errors.setLine(name.getRow());
		final String n = name.getValue();
		if (IFinancialStatementCategory.NAME_CONSTRAINT.validate("name", n, errors, ValidatorContext.ADD)) {
			if (XmlNode.<ImportContext>getContext(session).getNameList().add(n))
				uname = n.toUpperCase();
			else
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
		}
		if (active == null)
			active = false;
	}

}