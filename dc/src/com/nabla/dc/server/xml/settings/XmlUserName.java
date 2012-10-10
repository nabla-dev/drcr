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

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
class XmlUserName extends Node {

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
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
			errors.add(getRow(), FIELD, CommonServerErrors.INVALID_VALUE);
		else if (IUser.NAME_CONSTRAINT.validate(getRow(), FIELD, value, errors, ValidatorContext.ADD) &&
					!ctx.getNameList().add(value))
			errors.add(getRow(), FIELD, CommonServerErrors.DUPLICATE_ENTRY);
	}

}