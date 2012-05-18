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

import com.nabla.dc.client.model.options.RoleDefinitionModel;
import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.client.ui.options.RoleListUi;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.model.RoleRecord;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.shared.auth.IRolePrivileges;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
public class RoleList extends AbstractTabPresenter<RoleList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		@IRequiredRole(IRolePrivileges.ROLE_ADD) HideableCommand addRecord();
		@IRequiredRole(IRolePrivileges.ROLE_REMOVE) HideableCommand removeRecord();
		Command reload();
		Command savePreferences();
		@IRequiredRole(IRolePrivileges.ROLE_EDIT) RoleRecordCommand editDefinition();
		@IRequiredRole(IRolePrivileges.ROLE_EDIT) CommandUiManager edit();
		@IRequiredRole(IRolePrivileges.ROLE_ADD) CommandUiManager add();
	}

	public interface IDisplay extends ITabDisplay {
		void addRecord();
		void removeSelectedRecords(IListGridConfirmAction confirmUi);
		void reload();
		void savePreferences();
		ICommandSet getCommands();
	}

//	@Inject private PrintManager					printerManager;

	public RoleList(final IDisplay display) {
		super(display);
	}

	public RoleList() {
		this(new RoleListUi());
	}

	@Override
	protected void onBind() {
		final ICommandSet cmd = display.getCommands();
		registerSlot(cmd.addRecord(), onAddRecord);
		registerSlot(cmd.removeRecord(), onRemoveSelectedRecords);
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.editDefinition(), onEditDefinition);
		cmd.updateUi();

	//	printerManager.bind(cmd, this, BuiltInReports.ROLE_LIST);
	}

	private final ISlot onRemoveSelectedRecords = new ISlot() {
		@Override
		public void invoke() {
			display.removeSelectedRecords(onConfirmRemoveRecord);
		}
	};

	private final IListGridConfirmAction onConfirmRemoveRecord = new IListGridConfirmAction() {
		@Override
		public void confirmRemoveRecords(final ListGridRecord[] records, final com.google.gwt.user.client.Command onSuccess) {
			final RoleRecord role = new RoleRecord(records[0]);
			Application.getInstance().getMessageBox().ask(Resource.messages.confirmRemoveRoles(records.length, role.getName()), onSuccess);
		}
	};

	private final ISlot onAddRecord = new ISlot() {
		@Override
		public void invoke() {
			display.addRecord();
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

	private final ISlot1<RoleRecord> onEditDefinition = new ISlot1<RoleRecord>() {
		@Override
		public void invoke(final RoleRecord role) {
			new RoleDefinitionDialog(new RoleDefinitionModel(role.getId()), role.getName()).revealDisplay();
		}
	};

}
