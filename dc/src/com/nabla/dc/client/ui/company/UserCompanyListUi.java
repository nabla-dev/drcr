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
import com.nabla.dc.client.model.company.UserCompanyListModel;
import com.nabla.dc.client.presenter.company.UserCompanyList;
import com.nabla.dc.client.presenter.company.UserCompanyList.ICommandSet;
import com.nabla.dc.client.ui.Resource;
import com.nabla.wapp.client.command.ICurrentListGridRecordProvider;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.ListGrid;
import com.nabla.wapp.client.ui.Tab;

/**
 * @author nabla
 *
 */
public class UserCompanyListUi extends BindedTabDisplay<Tab> implements UserCompanyList.IDisplay {

	interface Binder extends UiBinder<Tab, UserCompanyListUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	String					tabTitle;
	@UiField
	ICommandSet				cmd;
	@UiField(provided=true)
	UserCompanyListModel	model;
	@UiField
	ListGrid				list;

	public UserCompanyListUi(final Integer userId, final String userName) {
		this.tabTitle = Resource.messages.userCompanyListTitle(userName);
		this.model = new UserCompanyListModel(userId);
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
	public ICurrentListGridRecordProvider getCurrentRecordProvider() {
		return list;
	}

}
