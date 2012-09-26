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
package com.nabla.dc.client.model.options;


import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.data.UserRecord;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UserNameField;
import com.nabla.wapp.client.model.field.UserPasswordField;
import com.nabla.wapp.shared.command.AddUser;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IUser;

/**
 * @author nabla
 *
 */
public class AddUserModel extends CModel<UserRecord> {

	static public class Fields {
		public String userName() { return IUser.NAME; }
		public String password() { return IUser.PASSWORD; }
		public String confirmPassword() { return IUser.CONFIRM_PASSWORD; }
	}

	private static final Fields	fields = new Fields();

	public AddUserModel() {
		super(UserRecord.factory);

		setFields(
			new UserNameField(),
			new UserPasswordField(),
			new IdField(),
			new BooleanField(IUser.ACTIVE, FieldAttributes.OPTIONAL),
			new TextField(IUser.LAST_LOGIN, FieldAttributes.OPTIONAL)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final UserRecord user) {
		return new AddUser(user.getName(), user.getPassword());
	}

}
