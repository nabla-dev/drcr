package com.nabla.dc.server.xml.settings;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.TXmlNode;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IUser;

@Root
class XmlUserName extends TXmlNode<ImportContext> {

	public static final String FIELD = "name";

	@Text
	String	value;

	public XmlUserName() {}

	public XmlUserName(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	protected void doValidate(final ImportContext ctx, final ICsvErrorList errors) throws DispatchException {
		if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
			errors.add(FIELD, CommonServerErrors.INVALID_VALUE);
		else if (IUser.NAME_CONSTRAINT.validate(FIELD, value, errors) &&
					!ctx.getNameList().add(value))
			errors.add(FIELD, CommonServerErrors.DUPLICATE_ENTRY);
	}

}