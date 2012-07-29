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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.nabla.dc.client.model.fixed_asset.CompanyFixedAssetCategoryRecord;
import com.nabla.dc.shared.model.fixed_asset.CompanyFixedAssetCategoryTree;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.data.IValueStore;
import com.nabla.wapp.client.model.data.ValueStoreWrapper;
import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * The <code></code> object is used to
 *
 */
public class CompanyFixedAssetCategoryTreeGrid extends TreeGridItem implements IValueStore<CompanyFixedAssetCategoryTree>, CellFormatter, CellSavedHandler {

	private static final String	TRUE_IMG = Canvas.imgHTML("[SKINIMG]/DynamicForm/checked.gif");
	private static final String	FALSE_IMG = Canvas.imgHTML("[SKINIMG]/DynamicForm/unchecked.gif");
	private static final String	NULL_IMG = Canvas.imgHTML("[SKINIMG]/DynamicForm/partialcheck.gif");

	private static final Logger	log = LoggerFactory.getLog(CompanyFixedAssetCategoryDialogUi.class);

	private boolean				loopGuard = false;

	public CompanyFixedAssetCategoryTreeGrid() {
		super();
		setCanEdit(true);
		setModelType(TreeModelType.CHILDREN);
		setColSpan(1);
		setShowHeader(false);
		setShowConnectors(true);
		setShowDropIcons(true);
		setShowSelectedStyle(true);
		setLoadDataOnDemand(false);
		setCanAcceptDroppedRecords(true);
		setCanDragRecordsOut(true);
		setCanDropOnLeaves(false);
		setCanReorderRecords(true);
	}

	public void postCreate(final String activeColumnName) {
		final CanvasItem wrapper = getCanvasItem();
		Assert.notNull(wrapper);
		// wrap value in a record otherwise I get a javascript error
		wrapper.storeValue(new ValueStoreWrapper<CompanyFixedAssetCategoryTree>(CompanyFixedAssetCategoryTreeGrid.this));

		final ListGridField field = getField(activeColumnName);
		field.setCellFormatter(this);
		field.addCellSavedHandler(this);
	}

	@Override
	public CompanyFixedAssetCategoryTree get() {
		final CompanyFixedAssetCategoryTree ret = new CompanyFixedAssetCategoryTree();
		for (ListGridRecord r : getRecords()) {
			final CompanyFixedAssetCategoryRecord child = new CompanyFixedAssetCategoryRecord(r);
			if (child.isFolder())
				continue;
			final Integer parentId = child.getParentId();
			Map<Integer, Boolean> children = ret.get(parentId);
			if (children == null) {
				children = new HashMap<Integer, Boolean>();
				ret.put(parentId, children);
			}
			children.put(child.getId(), child.getActive());
		}
		return ret;
	}

	@Override
	public String format(Object value, @SuppressWarnings("unused") ListGridRecord record, @SuppressWarnings("unused") int rowNum, @SuppressWarnings("unused") int colNum) {
		Boolean b = (Boolean)value;
		return "<span>" + ((b == null) ? NULL_IMG : (b ? TRUE_IMG : FALSE_IMG)) + "</span>";
	}

	@Override
	public void onCellSaved(CellSavedEvent event) {
		if (!loopGuard) {
			loopGuard = true;
			final CompanyFixedAssetCategoryRecord category = new CompanyFixedAssetCategoryRecord(event.getRecord());
			if (category.isFolder()) {
				final Boolean value = (Boolean) event.getNewValue();
				final String parentId = category.getStringId();
				for (ListGridRecord r : getRecords()) {
					final CompanyFixedAssetCategoryRecord record = new CompanyFixedAssetCategoryRecord(r);
					if (parentId.equals(record.getParentStringId()) && value != record.getActive()) {
						record.setActive(value);
						log.fine("updating " + record.getName() + " to " + value);
						updateData(record);
					}
				}
			} else {
				// record returned by event is not a TreeNode !!!
				final TreeNode node = getTree().findById(category.getStringId());
				final CompanyFixedAssetCategoryRecord parent = new CompanyFixedAssetCategoryRecord(getTree().getParent(node));
				final String parentId = parent.getStringId();
				Boolean value = null;
				for (ListGridRecord r : getRecords()) {
					final CompanyFixedAssetCategoryRecord record = new CompanyFixedAssetCategoryRecord(r);
					if (parentId.equals(record.getParentStringId())) {
						if (value == null)
							value = record.getActive();
						else if (value != record.getActive()) {
							value = null;
							break;
						}
					}
				}
				if (parent.getActive() != value) {
					parent.setActive(value);
					log.fine("updating " + parent.getName() + " to " + value);
					updateData(parent);
				}
			}
			loopGuard = false;
		}
	}
}
