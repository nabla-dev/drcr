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
import com.nabla.dc.client.model.fixed_asset.FixedAssetCategoryListModel;
import com.nabla.dc.client.presenter.fixed_asset.AddFixedAssetCategoryDialog;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class AddFixedAssetCategoryDialogUi extends BindedModalDialog implements AddFixedAssetCategoryDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, AddFixedAssetCategoryDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	FixedAssetCategoryListModel	model;
	@UiField
	Form						form;

	public AddFixedAssetCategoryDialogUi(final FixedAssetCategoryListModel model) {
		this.model = model;
		this.create(uiBinder, this);
		this.form.editNewRecord();
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

}
