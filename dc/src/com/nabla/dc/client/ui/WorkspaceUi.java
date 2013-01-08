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
package com.nabla.dc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.presenter.Workspace;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.binder.BindedCanvasDisplay;
import com.nabla.wapp.client.ui.Label;
import com.nabla.wapp.client.ui.TabDisplaySet;
import com.nabla.wapp.client.ui.VLayout;
import com.nabla.wapp.shared.signal.Signal;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;


public class WorkspaceUi extends BindedCanvasDisplay<VLayout> implements Workspace.IDisplay {

	interface Binder extends UiBinder<VLayout, WorkspaceUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField TabDisplaySet	tabs;
	@UiField Label			userName;
	@UiField Label			logoutButton;

	private final Signal	sigLogout = new Signal();

	public WorkspaceUi(final String userName) {
		this.create(uiBinder, this);
		setUserName(userName);
		logoutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") ClickEvent event) {
				sigLogout.fire();
			}
		});
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
	public ISlotManager getLogoutSlots() {
		return sigLogout;
	}

	@Override
	public void setUserName(String userName) {
		this.userName.setContents(Resource.messages.userloggedInAs(userName));
	}

}
