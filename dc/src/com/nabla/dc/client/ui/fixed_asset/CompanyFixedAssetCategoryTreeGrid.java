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

import com.nabla.dc.shared.model.fixed_asset.CompanyFixedAssetCategoryTree;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.data.IValueStore;
import com.nabla.wapp.client.model.data.ValueStoreWrapper;
import com.nabla.wapp.client.ui.form.TreeGridItem;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.fields.CanvasItem;

/**
 * The <code></code> object is used to
 *
 */
public class CompanyFixedAssetCategoryTreeGrid extends TreeGridItem implements IValueStore<CompanyFixedAssetCategoryTree> {

	public CompanyFixedAssetCategoryTreeGrid() {
		super();
		setCanEdit(true);
		setModelType(TreeModelType.CHILDREN);
		setColSpan(1);
		this.setShowHeader(false);
		setShowConnectors(true);
		setShowDropIcons(true);
		setShowSelectedStyle(true);
		setLoadDataOnDemand(false);
		this.setCanAcceptDroppedRecords(true);
		this.setCanDragRecordsOut(true);
		this.setCanDropOnLeaves(false);
		this.setDragDataAction(DragDataAction.MOVE);
		this.setCanReorderRecords(true);

		addDrawHandler(new DrawHandler() {
			@Override
			public void onDraw(@SuppressWarnings("unused") final DrawEvent event) {
				final CanvasItem wrapper = getCanvasItem();
				Assert.notNull(wrapper);
				// wrap value in a record otherwise I get a javascript error
				wrapper.storeValue(new ValueStoreWrapper<CompanyFixedAssetCategoryTree>(CompanyFixedAssetCategoryTreeGrid.this));
			}
		});
	}

	@Override
	public CompanyFixedAssetCategoryTree get() {
		return null;
	}

}
