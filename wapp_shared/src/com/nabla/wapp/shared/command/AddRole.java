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
package com.nabla.wapp.shared.command;

import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.model.IRole;

/**
 * @author nabla
 *
 */
public class AddRole implements IAction<StringResult>, IRole {

	private static final long serialVersionUID = 1L;

	@IRecordField(unique=true)
	String	name;

	protected AddRole() {}	// for serialization only

	public AddRole(final String name) {
		this.name = name;
	}

	public boolean validate(final IErrorList errors) {
		return NAME_CONSTRAINT.validate(NAME, name, errors);
	}

	public String getName() {
		return name;
	}
}
