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
package com.nabla.dc.client.ui.company.settings;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.company.settings.CompanyUserListModel;
import com.nabla.dc.client.presenter.company.settings.CompanyUserList;
import com.nabla.dc.client.presenter.company.settings.CompanyUserList.ICommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.model.UserRecord;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.ListGrid;
import com.nabla.wapp.client.ui.Tab;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class CompanyUserListUi extends BindedTabDisplay<Tab> implements CompanyUserList.IDisplay, ICurrentRecordProvider<UserRecord> {

	interface Binder extends UiBinder<Tab, CompanyUserListUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	ICommandSet				cmd;
	@UiField(provided=true)
	CompanyUserListModel	model;
	@UiField
	ListGrid				list;

	public CompanyUserListUi(final Integer companyId) {
		this.model = new CompanyUserListModel(companyId);
		this.create(uiBinder, this);
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
		list.saveViewState();
	}

	@Override
	public ICurrentRecordProvider<UserRecord> getCurrentRecordProvider() {
		return this;
	}

	@Override
	public UserRecord getCurrentRecord() {
		final Record record = list.getCurrentRecord();
		return (record == null) ? null : new UserRecord(record);
	}
}
