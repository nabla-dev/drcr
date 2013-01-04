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

import com.google.gwt.core.client.GWT;
import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.presenter.fixed_asset.AssetList;
import com.nabla.dc.client.presenter.fixed_asset.CompanyFixedAssetCategoryDialog;
import com.nabla.dc.client.ui.company.CompanyUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.HideableCommand;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.general.AbstractRunAsyncCallback;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.TabManager;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class Company extends AbstractTabPresenter<Company.IDisplay> implements ITabManager {

	public interface ICommandSet extends IBasicCommandSet {
		@IRequiredRole(IPrivileges.COMPANY_EDIT) HideableCommand changeLogo();
		@IRequiredRole(IPrivileges.COMPANY_USER_VIEW) HideableCommand editUsers();
		@IRequiredRole(IPrivileges.COMPANY_TAX_RATE_VIEW) HideableCommand editTaxRates();
		@IRequiredRole(IPrivileges.ACCOUNT_VIEW) HideableCommand editAccounts();
		@IRequiredRole(IPrivileges.PERIOD_END_VIEW) HideableCommand editPeriodEnds();
		@IRequiredRole(IPrivileges.COMPANY_ASSET_CATEGORY_EDIT) HideableCommand editAssetCategories();
		@IRequiredRole(IPrivileges.FA_COMPANY_ASSET_VIEW) HideableCommand editFixedAssets();
	}

	public interface IDisplay extends ITabDisplay {
		ISlotManager1<ITabDisplay> getTabClosedSlots();
		void addTab(final ITabDisplay tab);
		ICommandSet getCommands();
	}

	private final Integer		companyId;
	private final TabManager	tabs = new TabManager();

	public Company(final Integer companyId, final IDisplay display) {
		super(display);
		this.companyId = companyId;
	}

	public Company(final Integer companyId, final String companyName) {
		this(companyId, new CompanyUi(companyName));
	}

	@Override
	public void bind() {
		super.bind();
		registerHandler(getDisplay().getTabClosedSlots().connect(tabs.getTabClosedSlot()));
		final ICommandSet cmd = getDisplay().getCommands();
		registerSlot(cmd.changeLogo(), onChangeLogo);
		registerSlot(cmd.editUsers(), onEditUsers);
		registerSlot(cmd.editTaxRates(), onEditTaxRates);
		registerSlot(cmd.editAccounts(), onEditAccounts);
		registerSlot(cmd.editPeriodEnds(), onEditPeriodEnds);
		registerSlot(cmd.editAssetCategories(), onEditAssetCategories);
		registerSlot(cmd.editFixedAssets(), onEditFixedAssets);
		cmd.updateUi();
	}

	@Override
	public void unbind() {
		tabs.clear();
		super.unbind();
	}

	@Override
	public <D extends ITabDisplay> void addTab(final AbstractTabPresenter<D> tab) {
		getDisplay().addTab(tabs.add(tab));
	}

	private final ISlot onChangeLogo = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					new ChangeLogoDialog(companyId).revealDisplay();
				}
			});
		}
	};

	private final ISlot onEditUsers = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					addTab(new CompanyUserList(companyId));
				}
			});
		}
	};

	private final ISlot onEditTaxRates = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					new TaxRateListDialog(companyId).revealDisplay();
				}
			});
		}
	};

	private final ISlot onEditAccounts = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					addTab(new AccountList(companyId));
				}
			});
		}
	};

	private final ISlot onEditPeriodEnds = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					addTab(new PeriodEndList(companyId));
				}
			});
		}
	};

	private final ISlot onEditAssetCategories = new ISlot() {
		@Override
		public void invoke() {
			new CompanyFixedAssetCategoryDialog(companyId).revealDisplay();
		}
	};

	private final ISlot onEditFixedAssets = new ISlot() {
		@Override
		public void invoke() {
			GWT.runAsync(new AbstractRunAsyncCallback() {
				@Override
				public void onSuccess() {
					addTab(new AssetList(companyId, Company.this));
				}
			});
		}
	};
}
