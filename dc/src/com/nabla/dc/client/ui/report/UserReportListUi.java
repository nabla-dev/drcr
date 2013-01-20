/**
* Copyright 2013 nabla
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
package com.nabla.dc.client.ui.report;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.presenter.report.UserReportList;
import com.nabla.dc.client.presenter.report.UserReportList.ICommandSet;
import com.nabla.dc.shared.report.ReportCategories;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.ListGrid;
import com.nabla.wapp.client.ui.Tab;
import com.nabla.wapp.report.client.model.UserReportListModel;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;

public class UserReportListUi extends BindedTabDisplay<Tab> implements UserReportList.IDisplay {

	interface Binder extends UiBinder<Tab, UserReportListUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	ICommandSet				cmd;
	@UiField(provided=true)
	UserReportListModel		model;
	@UiField
	ListGrid				list;

	public UserReportListUi(final ReportCategories category) {
		model = new UserReportListModel(category.toString());
		this.create(uiBinder, this);
		list.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				list.selectRecord(event.getRecord());
				cmd.print().fire();
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
		list.saveViewState();
	}

	@Override
	public Set<Integer> getSelectedRecords() {
		return new HashSet<Integer>(list.getSelectedRecordIds());
	}
}
