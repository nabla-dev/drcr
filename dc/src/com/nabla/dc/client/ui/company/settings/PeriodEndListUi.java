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
import com.nabla.dc.client.model.company.settings.PeriodEndTreeModel;
import com.nabla.dc.client.presenter.company.settings.PeriodEndList;
import com.nabla.dc.client.presenter.company.settings.PeriodEndList.ICommandSet;
import com.nabla.dc.client.ui.Resource;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.Tab;
import com.nabla.wapp.client.ui.TreeGrid;

/**
 * @author nabla
 *
 */
public class PeriodEndListUi extends BindedTabDisplay<Tab> implements PeriodEndList.IDisplay {

	interface Binder extends UiBinder<Tab, PeriodEndListUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	String				tabTitle;
	@UiField(provided=true)
	final PeriodEndTreeModel	model;
	@UiField
	ICommandSet					cmd;
	@UiField
	TreeGrid					tree;

	public PeriodEndListUi(final Integer companyId) {
		this(companyId, "");
	}

	public PeriodEndListUi(final Integer companyId, final String companyName) {
		this.tabTitle = Resource.messages.periodEndListTitle(companyName);
		this.model = new PeriodEndTreeModel(companyId);
		this.create(uiBinder, this);
	}

	@Override
	public void reload() {
		tree.reload();
	}

	@Override
	public ICommandSet getCommands() {
		return cmd;
	}

}
