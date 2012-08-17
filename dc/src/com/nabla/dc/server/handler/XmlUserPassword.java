package com.nabla.dc.server.handler;

import java.util.Map;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IUser;

@Root
class XmlUserPassword {

	public static final String FIELD = "password";

	@Text
	String	value;

	public String getValue() {
		return value;
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		IUser.PASSWORD_CONSTRAINT.validate(FIELD, value, Importer.getErrors(session));
	}

}