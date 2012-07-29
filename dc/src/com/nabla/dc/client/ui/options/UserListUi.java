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
package com.nabla.dc.client.ui.options;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.options.UserListModel;
import com.nabla.dc.client.presenter.options.UserList;
import com.nabla.dc.client.presenter.options.UserList.ICommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.model.data.UserRecord;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.DeletedRecordGridFormatter;
import com.nabla.wapp.client.ui.ListGrid;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.client.ui.Tab;
import com.nabla.wapp.shared.command.AbstractRestore;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DSOperationType;

/**
 * @author nabla
 *
 */
public class UserListUi extends BindedTabDisplay<Tab> implements UserList.IDisplay, ICurrentRecordProvider<UserRecord> {

	interface Binder extends UiBinder<Tab, UserListUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	ICommandSet		cmd;
	@UiField
	UserListModel	model;
	@UiField
	ListGrid		list;

	public UserListUi() {
		this.create(uiBinder, this);
		if (Application.getInstance().getUserSessionManager().isRoot())
			list.setCellCSSTextFormatter(new DeletedRecordGridFormatter());
	}

	@Override
	public void addRecord(final Record record) {
		model.updateCache(record, DSOperationType.ADD);
	}

	@Override
	public void removeSelectedRecords(final IListGridConfirmAction confirmUi) {
		list.removeSelectedRecords(confirmUi);
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
	public boolean restoreSelectedRecords(final AbstractRestore command) {
		return list.onRestore(command);
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
