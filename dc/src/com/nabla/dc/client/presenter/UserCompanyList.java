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
package com.nabla.dc.client.presenter;

import com.google.gwt.core.client.GWT;
import com.nabla.dc.client.presenter.options.ChangeUserPasswordDialog;
import com.nabla.dc.client.presenter.options.RoleList;
import com.nabla.dc.client.presenter.options.UserList;
import com.nabla.dc.client.presenter.settings.CompanyList;
import com.nabla.dc.client.presenter.settings.TaxRateList;
import com.nabla.dc.client.ui.UserCompanyListUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.AbstractRunAsyncCallback;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.shared.slot.ISlot;

/**
 * @author nabla
 *
 */
public class UserCompanyList extends AbstractTabPresenter<UserCompanyList.IDisplay> {

	public interface ICommandSet extends IBasicCommandSet {
		Command reload();
		Command savePreferences();
		// SETTINGS
		@IRequiredRole(IPrivileges.COMPANY_VIEW) HideableCommand companyList();
		@IRequiredRole(IPrivileges.TAX_RATE_VIEW) HideableCommand taxCodeList();
		// OPTIONS
		Command changePassword();
		@IRequiredRole(IPrivileges.USER_VIEW) HideableCommand userList();
		@IRequiredRole(IPrivileges.ROLE_VIEW) HideableCommand roleList();
	//	@IRequiredRole(IPrivileges.REPORT_VIEW) Command reportList();
	}

	public interface IDisplay extends ITabDisplay {
		void reload();
		void savePreferences();
		ICommandSet getCommands();
	}

//	@Inject private PrintManager	printerManager;
	private final ITabManager		tabs;

	public UserCompanyList(final IDisplay display, final ITabManager tabs) {
		super(display);
		this.tabs = tabs;
	}

	public UserCompanyList(final ITabManager tabs) {
		this(new UserCompanyListUi(), tabs);
	}

	@Override
	protected void onBind() {
		final ICommandSet cmd = display.getCommands();
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.changePassword(), onChangePassword);
		registerSlot(cmd.roleList(), onRoleList);
		registerSlot(cmd.userList(), onUserList);
		registerSlot(cmd.companyList(), onCompanyList);
		registerSlot(cmd.taxCodeList(), onTaxRateList);
		cmd.updateUi();

//		printerManager.bind(cmd, this, BuiltInReports.ACCOUNT_LIST);
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

	private final ISlot onChangePassword = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					new ChangeUserPasswordDialog(Application.getInstance().getUserSessionManager().getSession().getName()).revealDisplay();
				}
			});
		}
	};

	private final ISlot onRoleList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new RoleList());
				}
			});
		}
	};

	private final ISlot onUserList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new UserList());
				}
			});
		}
	};

	private final ISlot onCompanyList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new CompanyList(tabs));
				}
			});
		}
	};

	private final ISlot onTaxRateList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new TaxRateList());
				}
			});
		}
	};
}
