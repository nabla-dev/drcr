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
import com.nabla.wapp.client.model.UserNameField;
import com.nabla.wapp.client.model.UserPasswordField;
import com.nabla.wapp.client.model.UserRecord;
import com.nabla.wapp.shared.command.ChangeUserPassword;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IUser;

/**
 * @author nabla
 *
 */
public class ChangeUserPasswordModel extends CModel<UserRecord> {

	static public class Fields {
		public String userName() { return IUser.NAME; }
		public String password() { return IUser.PASSWORD; }
		public String confirmPassword() { return IUser.CONFIRM_PASSWORD; }
	}

	public ChangeUserPasswordModel() {
		super(UserRecord.factory);

		setFields(new UserNameField(), new UserPasswordField());
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final UserRecord record) {
		return new ChangeUserPassword(record.getName(), record.getPassword());
	}

}
