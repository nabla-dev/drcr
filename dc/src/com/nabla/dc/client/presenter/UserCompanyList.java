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
import com.nabla.dc.client.model.UserCompanyRecord;
import com.nabla.dc.client.presenter.company.Company;
import com.nabla.dc.client.presenter.options.ChangeUserPasswordDialog;
import com.nabla.dc.client.presenter.options.RoleList;
import com.nabla.dc.client.presenter.options.UserList;
import com.nabla.dc.client.presenter.settings.CompanyList;
import com.nabla.dc.client.presenter.settings.TaxRateList;
import com.nabla.dc.client.presenter.settings.fixed_asset.BalanceSheetCategoryList;
import com.nabla.dc.client.presenter.settings.fixed_asset.FixedAssetCategoryList;
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
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager1;

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
		@IRequiredRole(IPrivileges.FA_ASSET_CATEGORY_VIEW) HideableCommand fixedAssetCategoryList();
		@IRequiredRole(IPrivileges.FA_BS_CATEGORY_VIEW) HideableCommand bsCategoryList();
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
		ISlotManager1<UserCompanyRecord> getSelectedSlots();
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
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.reload(), onReload);
		registerSlot(cmd.savePreferences(), onSavePreferences);
		registerSlot(cmd.changePassword(), onChangePassword);
		registerSlot(cmd.roleList(), onRoleList);
		registerSlot(cmd.userList(), onUserList);
		registerSlot(cmd.companyList(), onCompanyList);
		registerSlot(cmd.taxCodeList(), onTaxRateList);
		registerSlot(cmd.fixedAssetCategoryList(), onFixedAssetCategoryList);
		registerSlot(cmd.bsCategoryList(), onBalanceSheetCategoryList);
		cmd.updateUi();
		getDisplay().getSelectedSlots().connect(onOpenCompany);
//		printerManager.bind(cmd, this, BuiltInReports.ACCOUNT_LIST);
	}

	private final ISlot onReload = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().reload();
		}
	};

	private final ISlot1<UserCompanyRecord> onOpenCompany = new ISlot1<UserCompanyRecord>() {
		@Override
		public void invoke(final UserCompanyRecord record) {
			tabs.addTab(new Company(record.getId(), record.getName()));
		}
	};

	private final ISlot onSavePreferences = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().savePreferences();
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
					tabs.addTab(new UserList(tabs));
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

	private final ISlot onFixedAssetCategoryList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new FixedAssetCategoryList());
				}
			});
		}
	};

	private final ISlot onBalanceSheetCategoryList = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					tabs.addTab(new BalanceSheetCategoryList());
				}
			});
		}
	};
}
