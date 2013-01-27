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
package com.nabla.wapp.client.model;

import com.nabla.wapp.client.model.field.UserNameField;
import com.nabla.wapp.client.model.field.UserPasswordField;
import com.nabla.wapp.shared.model.IUser;


public class LoginModel extends Model {

	static public class Fields {
		public String userName() { return IUser.NAME; }
		public String password() { return IUser.PASSWORD; }
	}

	private final Fields	fields = new Fields();

	public LoginModel() {
		this.setFields(new UserNameField(), new UserPasswordField());
	}

	public Fields fields() {
		return fields;
	}

}
