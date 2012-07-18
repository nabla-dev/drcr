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
package com.nabla.dc.client.ui.company.settings.fixed_asset;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.company.settings.fixed_asset.AvailableFixedAssetCategoryTreeModel;
import com.nabla.dc.client.model.company.settings.fixed_asset.FixedAssetCategoryFormModel;
import com.nabla.dc.client.model.company.settings.fixed_asset.FixedAssetCategoryTreeModel;
import com.nabla.dc.client.presenter.company.settings.fixed_asset.FixedAssetCategoryDialog;
import com.nabla.dc.client.ui.options.RoleTreeGrid;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class FixedAssetCategoryDialogUi extends BindedTopDisplay<ModalDialog> implements FixedAssetCategoryDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, FixedAssetCategoryDialogUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final FixedAssetCategoryFormModel			formModel;
	@UiField
	Form										form;
	@UiField(provided=true)
	final AvailableFixedAssetCategoryTreeModel	availableModel;
	@UiField(provided=true)
	final FixedAssetCategoryTreeModel			model;
	@UiField(provided=true)
	final TreeGridItem							categories = new RoleTreeGrid();

	public FixedAssetCategoryDialogUi(final FixedAssetCategoryFormModel model) {
		this.formModel = model;
		this.availableModel = model.getAvailableTreeModel();
		this.model = model.getAssignedTreeModel();
		this.create(uiBinder, this);
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

}
