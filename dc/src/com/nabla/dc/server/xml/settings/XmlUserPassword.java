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

import java.util.Map;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
class XmlUserPassword {

	public static final String FIELD = "password";

	@Text
	String	value;

	public XmlUserPassword() {}

	public XmlUserPassword(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		IUser.PASSWORD_CONSTRAINT.validate(FIELD, value, XmlNode.getErrorList(session), ValidatorContext.ADD);
	}

}