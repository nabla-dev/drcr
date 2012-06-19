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
package com.nabla.dc.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.presenter.company.Company;
import com.nabla.dc.client.presenter.company.Company.ICommandSet;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.Tab;
import com.nabla.wapp.client.ui.TabDisplaySet;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class CompanyUi extends BindedTabDisplay<Tab> implements Company.IDisplay {

	interface Binder extends UiBinder<Tab, CompanyUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	String			companyName;
	@UiField
	TabDisplaySet	tabs;
	@UiField
	ICommandSet		cmd;

	public CompanyUi(final String companyName) {
		this.companyName = companyName;
		this.create(uiBinder, this);
	}

	@Override
	public void addTab(final ITabDisplay tab) {
		tabs.addTab(tab);
	}

	@Override
	public ISlotManager1<ITabDisplay> getTabClosedSlots() {
		return tabs.getTabClosedSlots();
	}

	@Override
	public ICommandSet getCommands() {
		return cmd;
	}
}
