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
import com.nabla.dc.client.presenter.UserCompanyList;
import com.nabla.dc.client.presenter.UserCompanyList.ICommandSet;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.Tab;
import com.nabla.wapp.client.ui.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickHandler;

/**
 * @author nabla
 *
 */
public class UserCompanyListUi extends BindedTabDisplay<Tab> implements UserCompanyList.IDisplay {

	interface Binder extends UiBinder<Tab, UserCompanyListUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	ICommandSet		cmd;
	@UiField
	TileGrid		list;

//	private final Signal1<UserCompanyRecord>	sigSelected = new Signal1<UserCompanyRecord>();

	public UserCompanyListUi() {
		this.create(uiBinder, this);
		list.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
	//			sigSelected.fire(UserCompanyRecord.factory.get(event.getRecord().getJsObj()));
			}
		});
	}

	@Override
	public void reload() {
		list.reload();
	}

	@Override
	public ICommandSet getCommands() {
		return cmd;
	}

	@Override
	public void savePreferences() {
//		list.saveViewState();
	}
/*
	@Override
	public ISlotManager1<UserCompanyRecord> getSelectedSlots() {
		return sigSelected;
	}
	*/
}
