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

import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;

/**
 * The <code></code> object is used to
 *
 */
public class CompanyFixedAssetCategoryTreeGrid extends TreeGridItem {

//	private static final Logger		log = LoggerFactory.getLog(CompanyFixedAssetCategoryTreeGrid.class);
//	private final SelectionDelta	delta = new SelectionDelta();

	public CompanyFixedAssetCategoryTreeGrid() {
		super();
		setCanEdit(true);
		setModelType(TreeModelType.CHILDREN);
		setColSpan(1);
		setShowConnectors(true);
		setLoadDataOnDemand(true);
	//	this.setCanAcceptDrop(true);
		this.setCanAcceptDroppedRecords(true);
		this.setCanDragRecordsOut(true);
		this.setCanDropOnLeaves(false);
		this.setDragDataAction(DragDataAction.MOVE);
		this.setCanReorderRecords(true);
	//	addCellSavedHandler(onAllocationChanged);
	}

/*
	private final CellSavedHandler onAllocationChanged = new CellSavedHandler() {
		@Override
		public void onCellSaved(CellSavedEvent event) {
			logger.fine("status change at column " + event.getColNum() + " = " + getFieldName(event.getColNum()));
           	final RoleDefinitionTreeRecord record = new RoleDefinitionTreeRecord(event.getRecord());
			final Integer roleId = record.getId();
			final Boolean isIncluded = (Boolean) event.getNewValue();
logger.fine("status changed for role " + roleId + " = " + isIncluded);
			if (isIncluded)
				delta.add(roleId);
			else
				delta.remove(roleId);
			final TreeGrid grid = (TreeGrid)event.getSource();
			Assert.notNull(grid);
			final CanvasItem wrapper = grid.getCanvasItem();
			Assert.notNull(wrapper);
			// wrap value in a record otherwise I get a javascript error
			wrapper.storeValue(new SelectionDeltaRecord(delta));
		}
	};*/
}
