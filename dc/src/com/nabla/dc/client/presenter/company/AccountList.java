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
import com.nabla.dc.client.model.company.AccountRecord;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.company.AccountListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.dc.shared.command.company.RestoreAccount;
import com.nabla.dc.shared.report.BuiltInReports;
import com.nabla.dc.shared.report.ReportParameterTypes;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequireRootRole;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.report.shared.parameter.IntegerParameterValue;
import com.nabla.wapp.shared.command.AbstractRestore;
import com.nabla.wapp.shared.slot.ISlot;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class AccountList extends AbstractTabPresenter<AccountList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		@IRequiredRole(IPrivileges.ACCOUNT_ADD) HideableCommand addRecord();
		@IRequiredRole(IPrivileges.ACCOUNT_REMOVE) HideableCommand removeRecord();
		@IRequireRootRole HideableCommand restoreRecord();
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.ACCOUNT_EDIT) CommandUiManager edit();
		@IRequiredRole(IPrivileges.ACCOUNT_ADD) HideableCommand importRecords();
	}

	public interface IDisplay extends ITabDisplay {
		void addRecord();
		void removeRecord(IListGridConfirmAction confirmUi);
		boolean restoreRecord(final AbstractRestore cmd);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
	}

	private final Integer	companyId;

	public AccountList(final Integer companyId, final IDisplay display) {
		super(display);
		this.companyId = companyId;
	}

	public AccountList(final Integer companyId) {
		this(companyId, new AccountListUi(companyId));
	}

	public AccountList(final Integer companyId, final String companyName) {
		this(companyId, new AccountListUi(companyId, companyName));
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
		registerSlot(cmd.importRecords(), onImport);
		cmd.updateUi();

		MyApplication.getInstance().getPrintManager().bind(cmd, this, BuiltInReports.ACCOUNT_LIST, new IntegerParameterValue(ReportParameterTypes.CompanyId.getParameterName(), companyId));
	}

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().addRecord();
		}
	};

	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().removeRecord(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final AccountRecord record = new AccountRecord(records[0]);
			Application.getInstance().getMessageBox().ask(
					Resource.messages.confirmRemoveAccounts(records.length, record.getName()),
					onSuccess);
		}
	};

	private final ISlot onRestoreRecord = new ISlot() {
		@Override
		public void invoke() {
			if (!getDisplay().restoreRecord(new RestoreAccount()))
				Application.getInstance().getMessageBox().error(Resource.strings.noDeletedAccountSelected());
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

	private final ISlot onImport = new ISlot() {
		@Override
		public void invoke() {
			new ImportAccountsWizard(companyId, onReload).revealDisplay();
		}
	};
}
