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
package com.nabla.dc.client.presenter.settings;

import com.nabla.dc.client.model.settings.CompanyRecord;
import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.presenter.company.settings.ChangeCompanyLogoDialog;
import com.nabla.dc.client.presenter.company.settings.CompanyTaxRateListDialog;
import com.nabla.dc.client.presenter.company.settings.CompanyUserList;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.settings.CompanyListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.dc.shared.command.settings.RestoreCompany;
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
import com.nabla.wapp.shared.command.AbstractRestore;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
public class CompanyList extends AbstractTabPresenter<CompanyList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		@IRequiredRole(IPrivileges.COMPANY_ADD) HideableCommand addRecord();
		@IRequiredRole(IPrivileges.COMPANY_REMOVE) HideableCommand removeRecord();
		@IRequireRootRole HideableCommand restoreRecord();
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.COMPANY_EDIT) CompanyRecordCommand changeLogo();
		@IRequiredRole(IPrivileges.COMPANY_USER_VIEW) CompanyRecordCommand editUsers();
		@IRequiredRole(IPrivileges.COMPANY_EDIT) CompanyRecordCommand editTaxRates();
		@IRequiredRole(IPrivileges.COMPANY_EDIT) CommandUiManager edit();
	}

	public interface IDisplay extends ITabDisplay {
		void addRecord();
		void removeRecord(IListGridConfirmAction confirmUi);
		boolean restoreRecord(final AbstractRestore cmd);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
	}

	private final ITabManager 		tabs;
/*
	@Inject private PrintManager						printerManager;
*/

	public CompanyList(final IDisplay display, final ITabManager tabs) {
		super(display);
		this.tabs = tabs;
	}

	public CompanyList(final ITabManager tabs) {
		this(new CompanyListUi(), tabs);
	}

	@Override
	protected void onBind() {
		final ICommandSet cmd = display.getCommands();
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.removeRecord(), onRemoveRecord);
		registerSlot(cmd.restoreRecord(), onRestoreRecord);
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.changeLogo(), onChangeLogo);
		registerSlot(cmd.editUsers(), onEditUsers);
		registerSlot(cmd.editTaxRates(), onEditTaxRates);
		cmd.updateUi();

//		printerManager.bind(cmd, this, BuiltInReports.USER_LIST);
	}

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			display.addRecord();
		}
	};

	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
			display.removeRecord(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final CompanyRecord record = new CompanyRecord(records[0]);
			Application.getInstance().getMessageBox().ask(
					Resource.messages.confirmRemoveCompanies(records.length, record.getName()),
					onSuccess);
		}
	};

	private final ISlot onRestoreRecord = new ISlot() {
		@Override
		public void invoke() {
			if (!display.restoreRecord(new RestoreCompany()))
				Application.getInstance().getMessageBox().error(Resource.strings.noDeletedCompanySelected());
		}
	};

	private final ISlot onReload = new ISlot() {
		@Override
		public void invoke() {
			display.reload();
		}
	};

	private final ISlot onSavePreferences = new ISlot() {
		@Override
		public void invoke() {
			display.savePreferences();
		}
	};

	private final ISlot1<CompanyRecord> onChangeLogo = new ISlot1<CompanyRecord>() {
		@Override
		public void invoke(final CompanyRecord record) {
			new ChangeCompanyLogoDialog(record.getId(), record.getName()).revealDisplay();
		}
	};

	private final ISlot1<CompanyRecord> onEditUsers = new ISlot1<CompanyRecord>() {
		@Override
		public void invoke(final CompanyRecord record) {
			tabs.addTab(new CompanyUserList(record.getId()));
		}
	};

	private final ISlot1<CompanyRecord> onEditTaxRates = new ISlot1<CompanyRecord>() {
		@Override
		public void invoke(final CompanyRecord record) {
			new CompanyTaxRateListDialog(record.getId(), record.getName()).revealDisplay();
		}
	};

}
