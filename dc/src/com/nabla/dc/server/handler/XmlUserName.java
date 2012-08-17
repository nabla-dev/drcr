package com.nabla.dc.server.handler;

import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IUser;

@Root
class XmlUserName {

	public static final String FIELD = "name";

	@Text
	String	value;

	public String getValue() {
		return value;
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = Importer.getErrors(session);
		if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
			errors.add(FIELD, CommonServerErrors.INVALID_VALUE);
		IUser.NAME_CONSTRAINT.validate(FIELD, value, errors);
		final Set<String> names = Importer.getContext(session);
		Assert.notNull(names);
		if (names.contains(value))
			errors.add(FIELD, CommonServerErrors.DUPLICATE_ENTRY);
		else
			names.add(value);
	}

}