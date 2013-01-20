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
package com.nabla.dc.client.presenter.company;

import com.nabla.dc.client.MyApplication;
import com.nabla.dc.client.model.options.UserDefinitionFormModel;
import com.nabla.dc.client.presenter.options.RoleDefinitionDialog;
import com.nabla.dc.client.presenter.options.UserRecordCommand;
import com.nabla.dc.client.ui.company.CompanyUserListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.dc.shared.report.BuiltInReports;
import com.nabla.dc.shared.report.CompanyParameterValue;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.model.data.UserRecord;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;


public class CompanyUserList extends AbstractTabPresenter<CompanyUserList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.COMPANY_USER_EDIT) UserRecordCommand editRoles();
		@IRequiredRole(IPrivileges.COMPANY_USER_EDIT) CommandUiManager edit();
	}

	public interface IDisplay extends ITabDisplay {
		void reload();
		void savePreferences();
		ICommandSet getCommands();
		ICurrentRecordProvider<UserRecord> getCurrentRecordProvider();
	}

	private final Integer	companyId;

	public CompanyUserList(final Integer companyId, final IDisplay display) {
		super(display);
		this.companyId = companyId;
		getDisplay().getCommands().setObjectId(companyId);
	}

	public CompanyUserList(final Integer companyId) {
		this(companyId, new CompanyUserListUi(companyId));
	}

	public CompanyUserList(final Integer companyId, final String companyName) {
		this(companyId, new CompanyUserListUi(companyId, companyName));
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.editRoles(), onEditUserRoles);
		cmd.editRoles().setRecordProvider(getDisplay().getCurrentRecordProvider());
		cmd.updateUi();

		MyApplication.getInstance().getPrintManager().bind(cmd, this, BuiltInReports.COMPANY_USER_LIST, new CompanyParameterValue(companyId));
	}

	private final ISlot onReload = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().reload();
		}
	};

	private final ISlot onSavePreferences = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().savePreferences();
		}
	};

	private final ISlot1<UserRecord> onEditUserRoles = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord record) {
			new RoleDefinitionDialog(new UserDefinitionFormModel(companyId, record.getId())).revealDisplay();
		}
	};

}
