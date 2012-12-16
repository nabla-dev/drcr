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
package com.nabla.dc.client.presenter.options;

import com.nabla.dc.client.MyApplication;
import com.nabla.dc.client.model.options.UserDefinitionFormModel;
import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.presenter.company.UserCompanyList;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.options.UserListUi;
import com.nabla.dc.shared.report.BuiltInReports;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.command.IRequireRootRole;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.model.data.UserRecord;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.shared.auth.IRolePrivileges;
import com.nabla.wapp.shared.command.AbstractRestore;
import com.nabla.wapp.shared.command.RestoreUser;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
public class UserList extends AbstractTabPresenter<UserList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		@IRequiredRole(IRolePrivileges.USER_ADD) HideableCommand addRecord();
		@IRequiredRole(IRolePrivileges.USER_REMOVE) HideableCommand removeRecord();
		@IRequireRootRole HideableCommand restoreRecord();
		Command reload();
		Command savePreferences();
		@IRequiredRole(IRolePrivileges.USER_EDIT) UserRecordCommand changePassword();
		@IRequiredRole(IRolePrivileges.ROLE_EDIT) UserRecordCommand editRoles();
		@IRequiredRole(IRolePrivileges.USER_EDIT) UserRecordCommand editCompanies();
		@IRequiredRole(IRolePrivileges.USER_ADD) UserRecordCommand cloneRecord();

		@IRequiredRole(IRolePrivileges.USER_EDIT) CommandUiManager edit();
	}

	public interface IDisplay extends ITabDisplay {
		void addRecord(final Record record);
		void removeSelectedRecords(IListGridConfirmAction confirmUi);
		boolean restoreSelectedRecords(final AbstractRestore cmd);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
		ICurrentRecordProvider<UserRecord> getCurrentRecordProvider();
	}

	private final ITabManager 	tabs;

	public UserList(final IDisplay display, final ITabManager tabs) {
		super(display);
		this.tabs = tabs;
	}

	public UserList(final ITabManager tabs) {
		this(new UserListUi(), tabs);
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.removeRecord(), onRemoveRecord);
		registerSlot(cmd.restoreRecord(), onRestoreRecord);
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.changePassword(), onChangeUserPassword);
		cmd.changePassword().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.editRoles(), onEditUserRoles);
		cmd.editRoles().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.editCompanies(), onEditUserCompanies);
		cmd.editCompanies().setRecordProvider(getDisplay().getCurrentRecordProvider());
		registerSlot(cmd.cloneRecord(), onCloneRecord);
		cmd.cloneRecord().setRecordProvider(getDisplay().getCurrentRecordProvider());
		cmd.updateUi();

		MyApplication.getInstance().getPrintManager().bind(cmd, this, BuiltInReports.USER_LIST);
	}

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			final AddUserDialog dlg = new AddUserDialog();
			dlg.getSuccessSlots().connect(onRecordAdded);
			dlg.revealDisplay();
		}
	};

	private final ISlot1<UserRecord> onCloneRecord = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord user) {
			final CloneUserDialog dlg = new CloneUserDialog(user.getId());
			dlg.getSuccessSlots().connect(onRecordAdded);
			dlg.revealDisplay();
		}
	};

	private final ISlot1<UserRecord> onRecordAdded = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord user) {
			getDisplay().addRecord(user);
		}
	};

	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().removeSelectedRecords(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final UserRecord user = new UserRecord(records[0]);
			Application.getInstance().getMessageBox().ask(
					Resource.messages.confirmRemoveUsers(records.length, user.getName()),
					onSuccess);
		}
	};

	private final ISlot onRestoreRecord = new ISlot() {
		@Override
		public void invoke() {
			if (!getDisplay().restoreSelectedRecords(new RestoreUser()))
				Application.getInstance().getMessageBox().error(Resource.strings.noDeletedUserSelected());
		}
	};

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

	private final ISlot1<UserRecord> onChangeUserPassword = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord record) {
			new ChangeUserPasswordDialog(record.getName()).revealDisplay();
		}
	};

	private final ISlot1<UserRecord> onEditUserRoles = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord record) {
			new RoleDefinitionDialog(new UserDefinitionFormModel(record.getId())).revealDisplay();
		}
	};

	private final ISlot1<UserRecord> onEditUserCompanies = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord record) {
			tabs.addTab(new UserCompanyList(record.getId(), record.getName()));
		}
	};
}
