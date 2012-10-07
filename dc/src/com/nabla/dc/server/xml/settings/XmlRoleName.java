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
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IRole;
import com.nabla.wapp.shared.validator.ValidatorContext;

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
			IRole.NAME_CONSTRAINT.validate(FIELD, value, errors, ValidatorContext.ADD);
	}

}