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

import java.util.ArrayList;
import java.util.List;
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
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.TransferImgButton;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.ListGridItem;
import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.signal.Signal2;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;
import com.nabla.wapp.shared.slot.ISlotManager2;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

/**
 * @author nabla
 *
 */
public class CompanyFixedAssetCategoryDialogUi extends BindedTopDisplay<ModalDialog> implements CompanyFixedAssetCategoryDialog.IDisplay, RecordDropHandler, FolderDropHandler, CellSavedHandler {

	interface Binder extends UiBinder<ModalDialog, CompanyFixedAssetCategoryDialogUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	class CategoryDropSignal extends Signal2<CompanyFixedAssetCategoryRecord, List<FixedAssetCategoryRecord>> {}
	class CategoryReparentSignal extends Signal2<CompanyFixedAssetCategoryRecord, List<CompanyFixedAssetCategoryRecord>> {}
	class AvailableCategoryDropSignal extends Signal1<List<CompanyFixedAssetCategoryRecord>> {}

	@UiField(provided=true)
	final CompanyFixedAssetCategoryFormModel	formModel;
	@UiField
	Form										form;
	@UiField(provided=true)
	final AvailableFixedAssetCategoryListModel	availableModel;
	@UiField
	ListGridItem								availableCategories;
	@UiField(provided=true)
	final CompanyFixedAssetCategoryTreeModel	model;
	@UiField(provided=true)
	final TreeGridItem							categories = new CompanyFixedAssetCategoryTreeGrid();
	@UiField
	TransferImgButton							addButton;
	@UiField
	TransferImgButton							removeButton;

	private final CategoryDropSignal			sigCategoryDrop = new CategoryDropSignal();
	private final CategoryReparentSignal		sigCategoryReparent = new CategoryReparentSignal();
	private final AvailableCategoryDropSignal	sigRecordDrop = new AvailableCategoryDropSignal();
	private boolean							loopGuard = false;
	private static final Logger	log = LoggerFactory.getLog(CompanyFixedAssetCategoryDialogUi.class);
	public CompanyFixedAssetCategoryDialogUi(final Integer companyId) {
		formModel = new CompanyFixedAssetCategoryFormModel(companyId);
		availableModel = formModel.getAvailableTreeModel();
		model = formModel.getCategoryTreeModel();
		this.create(uiBinder, this);
		categories.addFolderDropHandler(this);
		categories.addCellSavedHandler(this);
		availableCategories.addRecordDropHandler(this);
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") ClickEvent event) {
				onAddCategory();
			}
		});
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") ClickEvent event) {
				onRemoveCategory();
			}
		});

	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public ISlotManager2<CompanyFixedAssetCategoryRecord, List<FixedAssetCategoryRecord>> getCategoryDropSlots() {
		return sigCategoryDrop;
	}

	@Override
	public ISlotManager1<List<CompanyFixedAssetCategoryRecord>> getAvailableCategoryDropSlots() {
		return sigRecordDrop;
	}

	@Override
	public ISlotManager2<CompanyFixedAssetCategoryRecord, List<CompanyFixedAssetCategoryRecord>> getCategoryReparentSlots() {
		return sigCategoryReparent;
	}

	@Override
	public void onRecordDrop(RecordDropEvent event) {
		event.cancel();
		final ListGridRecord[] records = event.getDropRecords();
		if (records != null && records.length > 0)
			sigRecordDrop.fire(arrayToList(records, CompanyFixedAssetCategoryRecord.factory));
	}

	@Override
	public void onFolderDrop(FolderDropEvent event) {
		event.cancel();
		if (event.getFolder() != null) {
			final TreeNode[] records = event.getNodes();
			if (records != null && records.length > 0) {
				final CompanyFixedAssetCategoryRecord parent = new CompanyFixedAssetCategoryRecord(event.getFolder());
				if (categories.equals(event.getSourceWidget())) {
					sigCategoryReparent.fire(parent, arrayToList(records, CompanyFixedAssetCategoryRecord.factory));
				} else {
					sigCategoryDrop.fire(parent, arrayToList(records, FixedAssetCategoryRecord.factory));
				}
			}
		}
	}

	@Override
	public void removeCategory(CompanyFixedAssetCategoryRecord record) {
		model.updateCache(record, DSOperationType.REMOVE);
	}

	@Override
	public void removeCategory(FixedAssetCategoryRecord record) {
		availableModel.updateCache(record, DSOperationType.REMOVE);
	}

	@Override
	public void addCategory(CompanyFixedAssetCategoryRecord record) {
		model.updateCache(record, DSOperationType.ADD);
	}

	@Override
	public void addCategory(FixedAssetCategoryRecord record) {
		availableModel.updateCache(record, DSOperationType.ADD);
	}

	private void onAddCategory() {
		final ListGridRecord[] records = availableCategories.getSelectedRecords();
		if (records != null && records.length > 0) {
			final TreeNode folder = (TreeNode)categories.getSelectedRecord();
			if (folder != null) {
				CompanyFixedAssetCategoryRecord parent = new CompanyFixedAssetCategoryRecord(folder);
				if (!parent.isFolder())
					parent = new CompanyFixedAssetCategoryRecord(categories.getTree().getParent(folder));
				sigCategoryDrop.fire(parent, arrayToList(records, FixedAssetCategoryRecord.factory));
			}
		}
	}

	private void onRemoveCategory() {
		final ListGridRecord[] records = categories.getSelectedRecords();
		if (records != null && records.length > 0)
			sigRecordDrop.fire(arrayToList(records, CompanyFixedAssetCategoryRecord.factory));
	}

	@Override
	public void onCellSaved(CellSavedEvent event) {
		if (!loopGuard) {
			log.fine("CALLED");
			loopGuard = true;
			final CompanyFixedAssetCategoryRecord record = new CompanyFixedAssetCategoryRecord(event.getRecord());
			if (record.isFolder()) {
				log.fine("CASCADE");
				final Boolean value = (Boolean) event.getNewValue();
				final String parentId = record.getStringId();
				for (ListGridRecord r : categories.getRecords()) {
					final CompanyFixedAssetCategoryRecord node = new CompanyFixedAssetCategoryRecord(r);
					if (parentId.equals(node.getParentStringId()) && value != node.getActive()) {
//						node.setActive(value);
						JSOHelper.deleteAttribute(node.getJsObj(), "active");
						log.fine("updating " + node.getName() + " to " + node.getActive());
						categories.updateData(node);
					}
				}
			} else {

			}
			loopGuard = false;
		}
	}

	private <T extends ListGridRecord, N extends ListGridRecord> List<T> arrayToList(final N[] records, final IRecordFactory<T> factory) {
		final List<T> list = new ArrayList<T>(records.length);
		for (N record : records)
			list.add(factory.get(record.getJsObj()));
		return list;
	}
}
