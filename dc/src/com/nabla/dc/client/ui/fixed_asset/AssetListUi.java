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
import com.nabla.dc.client.model.fixed_asset.AssetListModel;
import com.nabla.dc.client.model.fixed_asset.AssetRecord;
import com.nabla.dc.client.presenter.fixed_asset.AssetList;
import com.nabla.dc.client.presenter.fixed_asset.AssetList.ICommandSet;
import com.nabla.wapp.client.command.ICurrentRecordProvider;
import com.nabla.wapp.client.model.UpdateModelCacheOperations;
import com.nabla.wapp.client.mvp.binder.BindedTabDisplay;
import com.nabla.wapp.client.ui.ListGrid;
import com.nabla.wapp.client.ui.ListGrid.IListGridConfirmAction;
import com.nabla.wapp.client.ui.Tab;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class AssetListUi extends BindedTabDisplay<Tab> implements AssetList.IDisplay, ICurrentRecordProvider<AssetRecord> {

	interface Binder extends UiBinder<Tab, AssetListUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	ICommandSet				cmd;
	@UiField(provided=true)
	final AssetListModel	model;
	@UiField
	ListGrid				list;

	public AssetListUi(final Integer companyId) {
		this.model = new AssetListModel(companyId);
		this.create(uiBinder, this);
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
	public void savePreferences() {
		list.saveViewState();
	}

	@Override
	public void addRecord(Integer recordId) {
		model.updateCache(recordId, UpdateModelCacheOperations.ADD);
	}

	@Override
	public void updateRecord(Integer recordId) {
		model.updateCache(recordId);
	}

	@Override
	public ICurrentRecordProvider<AssetRecord> getCurrentRecordProvider() {
		return this;
	}

	@Override
	public AssetRecord getCurrentRecord() {
		final Record record = list.getCurrentRecord();
		return (record == null) ? null : new AssetRecord(record);
	}
}
