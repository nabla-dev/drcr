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

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.fixed_asset.AvailableFixedAssetCategoryListModel;
import com.nabla.dc.client.model.fixed_asset.CompanyFixedAssetCategoryFormModel;
import com.nabla.dc.client.model.fixed_asset.CompanyFixedAssetCategoryRecord;
import com.nabla.dc.client.model.fixed_asset.CompanyFixedAssetCategoryTreeModel;
import com.nabla.dc.client.model.fixed_asset.FixedAssetCategoryRecord;
import com.nabla.dc.client.presenter.fixed_asset.CompanyFixedAssetCategoryDialog;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

/**
 * @author nabla
 *
 */
public class CompanyFixedAssetCategoryDialogUi extends BindedTopDisplay<ModalDialog> implements CompanyFixedAssetCategoryDialog.IDisplay, FolderDropHandler {

	interface Binder extends UiBinder<ModalDialog, CompanyFixedAssetCategoryDialogUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final CompanyFixedAssetCategoryFormModel	formModel;
	@UiField
	Form										form;
	@UiField(provided=true)
	final AvailableFixedAssetCategoryListModel	availableModel;
	@UiField(provided=true)
	final CompanyFixedAssetCategoryTreeModel	model;
	@UiField(provided=true)
	final TreeGridItem							categories = new CompanyFixedAssetCategoryTreeGrid();

	private static final Logger		log = LoggerFactory.getLog(CompanyFixedAssetCategoryDialogUi.class);

	public CompanyFixedAssetCategoryDialogUi(final Integer companyId) {
		formModel = new CompanyFixedAssetCategoryFormModel(companyId);
		availableModel = formModel.getAvailableTreeModel();
		model = formModel.getCategoryTreeModel();
		categories.addFolderDropHandler(this);
		this.create(uiBinder, this);
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public void onFolderDrop(FolderDropEvent event) {
		if (event.getFolder() == null) {
			event.cancel();
			log.fine("stop dropping category to NO folder");
		} else {
			final CompanyFixedAssetCategoryRecord fsCategory = new CompanyFixedAssetCategoryRecord(event.getFolder());
			if (fsCategory.isNew()) {
				event.cancel();
				log.fine("stop dropping category to ROOT folder");
			} else {

			TreeNode[] nodes = event.getNodes();
			for (TreeNode node : nodes) {
				final FixedAssetCategoryRecord category = new FixedAssetCategoryRecord(node);
log.fine("dropping category " + category.getName() + " into " + fsCategory.getName());
			}
				event.cancel();	// handle the drop ourself
			}
		}
	}
}
