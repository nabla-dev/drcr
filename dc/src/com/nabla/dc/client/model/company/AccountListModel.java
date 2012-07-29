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
package com.nabla.dc.client.model.company;

import com.nabla.dc.shared.command.company.AddAccount;
import com.nabla.dc.shared.command.company.FetchAccountList;
import com.nabla.dc.shared.command.company.RemoveAccount;
import com.nabla.dc.shared.command.company.UpdateAccount;
import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.DeletedRecordField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class AccountListModel extends CModel<AccountRecord> {

	static public class Fields {
		public String code() { return IAccount.CODE; }
		public String name() { return IAccount.NAME; }
		public String cc() { return IAccount.COST_CENTRE; }
		public String dep() { return IAccount.DEPARTMENT; }
		public String active() { return IAccount.ACTIVE; }
		public String bs() { return IAccount.BALANCE_SHEET; }
	}

	private static final Fields	fields = new Fields();
	private final Integer		companyId;

	public AccountListModel(final Integer companyId) {
		super(AccountRecord.factory);

		this.companyId = companyId;
		setFields(
			new DeletedRecordField(),
			new IdField(),
			new TextField(fields.code(), IAccount.CODE_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.name(), IAccount.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.cc(), IAccount.CC_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(fields.dep(), IAccount.DEP_CONSTRAINT, FieldAttributes.OPTIONAL),
			new BooleanField(fields.bs()),
			new BooleanField(fields.active())
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAccountList(companyId);
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveAccount();
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final AccountRecord record) {
		return new UpdateAccount(record.getId(), record.getCode(), record.getName(), record.getCostCentre(), record.getDepartment(), record.isBalanceSheet(), record.getActive());
	}

	@Override
	public IAction<StringResult> getAddCommand(final AccountRecord record) {
		return new AddAccount(companyId, record.getCode(), record.getName(), record.getCostCentre(), record.getDepartment(), record.isBalanceSheet(), record.getActive());
	}
}
