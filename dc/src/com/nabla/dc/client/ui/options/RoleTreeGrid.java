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
package com.nabla.dc.client.ui.options;

import java.util.logging.Logger;

import com.nabla.dc.client.model.options.RoleDefinitionTreeRecord;
import com.nabla.dc.client.model.options.SelectionDeltaRecord;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.ui.TreeGrid;
import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.nabla.wapp.shared.general.SelectionDelta;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.grid.events.CellSavedEvent;
import com.smartgwt.client.widgets.grid.events.CellSavedHandler;

/**
 * The <code></code> object is used to
 *
 */
public class RoleTreeGrid extends TreeGridItem {

	private static final Logger	log = LoggerFactory.getLog(RoleTreeGrid.class);
	private final SelectionDelta	delta = new SelectionDelta();

	public RoleTreeGrid() {
		super();
		setCanEdit(true);
		setModelType(TreeModelType.CHILDREN);
		addCellSavedHandler(onAllocationChanged);
	}

	private final CellSavedHandler onAllocationChanged = new CellSavedHandler() {
		@Override
		public void onCellSaved(CellSavedEvent event) {
			log.fine("status change at column " + event.getColNum() + " = " + getFieldName(event.getColNum()));
           	final RoleDefinitionTreeRecord record = new RoleDefinitionTreeRecord(event.getRecord());
			final Integer roleId = record.getId();
			final Boolean isIncluded = (Boolean) event.getNewValue();
			log.fine("status changed for role " + roleId + " = " + isIncluded);
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
	};
}
