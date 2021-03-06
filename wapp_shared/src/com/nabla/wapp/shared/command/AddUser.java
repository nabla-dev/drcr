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
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.model.IUserTable;
import com.nabla.wapp.shared.validator.ValidatorContext;


public class AddUser implements IRecordAction<StringResult>, IUser {

	@IRecordField(name=IUserTable.NAME,unique=true)
	String	name;
	String	password;

	AddUser() {}	// for serialization only

	public AddUser(final String name, final String password) {
		this.name = name;
		this.password = password;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		int n = errors.size();
		NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.ADD);
		PASSWORD_CONSTRAINT.validate(PASSWORD, password, errors, ValidatorContext.ADD);
		return n == errors.size();
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

}
