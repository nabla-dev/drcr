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
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddUser implements IAction<StringResult>, IUser {

	private static final long serialVersionUID = 1L;

	@IRecordField(unique=true)
	String	name;
	String	password;

	protected AddUser() {}	// for serialization only

	public AddUser(final String name) {
		this.name = name;
	}

	public AddUser(final String name, final String password) {
		this.name = name;
		this.password = password;
	}

	public void validate() throws ValidationException {
		NAME_CONSTRAINT.validate(NAME, name);
		PASSWORD_CONSTRAINT.validate(PASSWORD, password);
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

}
