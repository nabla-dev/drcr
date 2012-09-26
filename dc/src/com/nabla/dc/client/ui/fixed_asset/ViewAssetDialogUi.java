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
import com.nabla.dc.client.model.fixed_asset.AssetRecord;
import com.nabla.dc.client.presenter.fixed_asset.ViewAssetDialog;
import com.nabla.dc.client.ui.IResource;
import com.nabla.dc.client.ui.Resource;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.TabSet;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class ViewAssetDialogUi extends BindedModalDialog implements ViewAssetDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, ViewAssetDialogUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	static final IResource		res = Resource.bundle;
	@UiField
	TabSet						tabs;
	@UiField(provided=true)
	final Model					model;
	@UiField
	Form						generalForm;
	@UiField(provided=true)
	final AssetAcquisitionModel					acquisitionModel;

	public ViewAssetDialogUi(AssetRecord asset, Integer assetRegisterId) {
		this.model = modelFactory.get(assetRegisterId);
		this.acquisitionModel = acquisitionModel;
		this.create(uiBinder, this);
		generalForm.editRecord(asset.getImpl());
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public void addTab(ITabDisplay tab) {
		tabs.addTab(tab.getImpl());
	}

}
