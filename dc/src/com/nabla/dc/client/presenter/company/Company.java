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

import com.nabla.dc.client.model.settings.CompanyRecord;
import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.presenter.company.settings.AccountList;
import com.nabla.dc.client.presenter.company.settings.ChangeCompanyLogoDialog;
import com.nabla.dc.client.presenter.company.settings.CompanyTaxRateListDialog;
import com.nabla.dc.client.presenter.company.settings.CompanyUserList;
import com.nabla.dc.client.presenter.settings.CompanyRecordCommand;
import com.nabla.dc.client.ui.company.CompanyUi;
import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.command.IRequiredRole;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.TabManager;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class Company extends AbstractTabPresenter<Company.IDisplay> implements ITabManager {

	public interface ICommandSet extends IBasicCommandSet {
		@IRequiredRole(IPrivileges.COMPANY_EDIT) CompanyRecordCommand changeLogo();
		@IRequiredRole(IPrivileges.COMPANY_USER_VIEW) CompanyRecordCommand editUsers();
		@IRequiredRole(IPrivileges.COMPANY_EDIT) CompanyRecordCommand editTaxRates();
		@IRequiredRole(IPrivileges.COMPANY_EDIT) CompanyRecordCommand editAccounts();
	}

	public interface IDisplay extends ITabDisplay {
		ISlotManager1<ITabDisplay> getTabClosedSlots();
		void addTab(final ITabDisplay tab);
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
	protected void onBind() {
		registerHandler(getDisplay().getTabClosedSlots().connect(tabs.getTabClosedSlot()));
	}

	@Override
	protected void onUnbind() {
		tabs.clear();
		super.onUnbind();
	}

	@Override
	public <D extends ITabDisplay> void addTab(final AbstractTabPresenter<D> tab) {
		getDisplay().addTab(tabs.add(tab));
	}

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

	private final ISlot1<CompanyRecord> onEditAccounts = new ISlot1<CompanyRecord>() {
		@Override
		public void invoke(final CompanyRecord record) {
			tabs.addTab(new AccountList(record.getId()));
		}
	};

}
