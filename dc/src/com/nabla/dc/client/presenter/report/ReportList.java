/**
* Copyright 2013 nabla
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
package com.nabla.dc.client.presenter.report;

import com.nabla.dc.client.ui.report.ReportListUi;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.report.shared.IReportPrivileges;
import com.nabla.wapp.shared.slot.ISlot;

public class ReportList extends AbstractTabPresenter<ReportList.IDisplay> {

	public interface ICommandSet extends IBasicCommandSet {
		@IRequiredRole(IReportPrivileges.REPORT_ADD) HideableCommand addRecord();
		@IRequiredRole(IReportPrivileges.REPORT_REMOVE) HideableCommand removeRecord();
		Command reload();
		Command savePreferences();

		@IRequiredRole(IReportPrivileges.REPORT_EDIT) CommandUiManager edit();
	}

	public interface IDisplay extends ITabDisplay {
/*		void addRecord(final Record record);
		void removeSelectedRecords(IListGridConfirmAction confirmUi);
		*/
		void reload();
		void savePreferences();
		ICommandSet getCommands();
//		ICurrentRecordProvider<UserRecord> getCurrentRecordProvider();
	}

	public ReportList(final IDisplay display) {
		super(display);
	}

	public ReportList() {
		this(new ReportListUi());
	}

	@Override
	public void bind() {
		super.bind();
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.removeRecord(), onRemoveRecord);

		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);

		cmd.updateUi();
	}

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
		}
	};
/*
	private final ISlot1<UserRecord> onRecordAdded = new ISlot1<UserRecord>() {
		@Override
		public void invoke(final UserRecord user) {
			getDisplay().addRecord(user);
		}
	};
*/
	private final ISlot onRemoveRecord = new ISlot() {
		@Override
		public void invoke() {
//			getDisplay().removeSelectedRecords(onConfirmRemoveRecord);
		}
	};
/*
	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final UserRecord user = new UserRecord(records[0]);
			Application.getInstance().getMessageBox().ask(
					Resource.messages.confirmRemoveUsers(records.length, user.getName()),
					onSuccess);
		}
	};
*/

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

}
