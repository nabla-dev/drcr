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
package com.nabla.dc.client.model.settings;


import com.nabla.dc.shared.command.settings.FetchCompanyUserList;
import com.nabla.dc.shared.command.settings.UpdateCompanyUser;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.UserRecord;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IUser;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class CompanyUserListModel extends CModel<UserRecord> {

	static public class Fields {
		public String name() { return IUser.NAME; }
		public String active() { return IUser.ACTIVE; }
	}

	private static final Fields	fields = new Fields();
	private final Integer		companyId;

	public CompanyUserListModel(final Integer companyId) {
		super(UserRecord.factory);

		this.companyId = companyId;
		setFields(
			new IdField(),
			new TextField(IUser.NAME, FieldAttributes.READ_ONLY),
			new BooleanField(IUser.ACTIVE)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyUserList(companyId);
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final UserRecord record) {
		return new UpdateCompanyUser(companyId, record.getId(), record.getActive());
	}

}
