package com.nabla.dc.server.xml.settings;

import java.util.Map;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IRole;

@Root
class XmlRoleName extends XmlString {

	public static final String FIELD = "name";

	public XmlRoleName() {}

	public XmlRoleName(final String value) {
		super(value);
	}

	@Override
	@Validate
	public void validate(final Map session) throws DispatchException {
		super.validate(session);
		final ICsvErrorList errors = getErrorList(session);
		if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
			errors.add(FIELD, CommonServerErrors.INVALID_VALUE);
		else
			IRole.NAME_CONSTRAINT.validate(FIELD, value, errors);
	}

}