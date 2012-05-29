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

import com.nabla.dc.client.presenter.ITabManager;
import com.nabla.dc.client.ui.company.CompanyUi;
import com.nabla.wapp.client.command.IBasicCommandSet;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.TabManager;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class Company extends AbstractTabPresenter<Company.IDisplay> implements ITabManager {

	public interface ICommandSet extends IBasicCommandSet {
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

	public Company(final Integer companyId) {
		this(companyId, new CompanyUi("company"));
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

}
