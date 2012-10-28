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
package com.nabla.dc.client.ui.fixed_asset;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.fixed_asset.TransactionListModel;
import com.nabla.dc.client.presenter.fixed_asset.TransactionList;
import com.nabla.dc.client.presenter.fixed_asset.TransactionList.ICommandSet;
import com.nabla.dc.client.ui.Resource;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.ListGrid;
import com.nabla.wapp.client.ui.Tab;

/**
 * @author nabla
 *
 */
public class TransactionListUi extends BindedTabDisplay<Tab> implements TransactionList.IDisplay {

	interface Binder extends UiBinder<Tab, TransactionListUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final String				tabTitle;
	@UiField
	ICommandSet					cmd;
	@UiField(provided=true)
	final TransactionListModel	model;
	@UiField
	ListGrid					list;

	public TransactionListUi(final Integer assetId, final String assetName) {
		this.tabTitle = Resource.messages.assetTransactionListTitle(assetName);
		model = new TransactionListModel(assetId);
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

}
