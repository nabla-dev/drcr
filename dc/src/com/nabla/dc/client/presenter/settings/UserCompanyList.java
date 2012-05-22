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

import com.nabla.dc.client.model.options.UserDefinitionModel;
import com.nabla.dc.client.model.settings.CompanyRecord;
import com.nabla.dc.client.presenter.options.RoleDefinitionDialog;
import com.nabla.dc.client.ui.settings.UserCompanyListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.CommandUiManager;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;

/**
 * @author nabla
 *
 */
public class UserCompanyList extends AbstractTabPresenter<UserCompanyList.IDisplay> {

	public interface ICommandSet extends IPrintCommandSet, IBasicCommandSet {
		Command reload();
		Command savePreferences();
		@IRequiredRole(IPrivileges.COMPANY_USER_EDIT) CompanyRecordCommand editRoles();
		@IRequiredRole(IPrivileges.COMPANY_USER_EDIT) CommandUiManager edit();
	}
	
	public interface IDisplay extends ITabDisplay {
		void reload();
		void savePreferences();
		ICommandSet getCommands();
	}

	private final Integer	userId;
	
	public UserCompanyList(final Integer userId, final IDisplay ui) {
		super(ui);
		this.userId = userId;
		display.getCommands().setObjectId(userId);
	}

	public UserCompanyList(final Integer userId) {
		this(userId, new UserCompanyListUi(userId));
	}

	@Override
	protected void onBind() {
		final ICommandSet cmd = display.getCommands();
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.editRoles(), onEditUserRoles);
		cmd.updateUi();
	}

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

	private final ISlot1<CompanyRecord> onEditUserRoles = new ISlot1<CompanyRecord>() {
		@Override
		public void invoke(final CompanyRecord record) {
			new RoleDefinitionDialog(new UserDefinitionModel(record.getId(), userId), "user name").revealDisplay();
		}
	};

}
