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
import com.nabla.wapp.client.model.field.DeletedRecordField;
import com.nabla.wapp.client.model.field.EnabledRecordField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UserNameField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.command.FetchUserList;
import com.nabla.wapp.shared.command.RemoveUser;
import com.nabla.wapp.shared.command.UpdateUser;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IUser;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class UserListModel extends CModel<UserRecord> {

	static public class Fields {
		public String name() { return IUser.NAME; }
		public String active() { return IUser.ACTIVE; }
		public String created() { return IUser.CREATED; }
		public String lastLogin() { return IUser.LAST_LOGIN; }
	}

	private static final Fields	fields = new Fields();

	public UserListModel() {
		super(UserRecord.factory);

		setFields(
			new EnabledRecordField(),
			new DeletedRecordField(),
			new IdField(),
			new UserNameField(),
			new BooleanField(IUser.ACTIVE),
			new TextField(IUser.LAST_LOGIN, FieldAttributes.OPTIONAL, FieldAttributes.READ_ONLY),
			new TextField(IUser.CREATED, FieldAttributes.OPTIONAL, FieldAttributes.READ_ONLY)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveUser();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchUserList();
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final UserRecord record) {
		return new UpdateUser(record.getId(), record.getName(), record.getActive());
	}

}
