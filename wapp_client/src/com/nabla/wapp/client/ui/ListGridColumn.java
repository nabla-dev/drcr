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
package com.nabla.wapp.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.JSHelper;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * @author nabla
 *
 */
public class ListGridColumn extends Widget {

	protected final ListGridField	impl = new ListGridField();

	ListGridField getField(@SuppressWarnings("unused") final ListGrid parent) {
		Assert.notNull(impl.getName());	// a unique name is required to save listgrid state
		return impl;
	}

	@Override
	public void setWidth(final String w) {
		impl.setWidth(w);
	}

	public void setName(final String name) {
		impl.setName(name);
	}

	public String getName() {
		return impl.getName();
	}

	public void setCanGroupBy(final Boolean canGroupBy) {
		impl.setCanGroupBy(canGroupBy);
	}

	public void setCanFreeze(final Boolean canFreeze) {
		impl.setCanFreeze(canFreeze);
	}

	public void setEmptyCellValue(final String value) {
		impl.setEmptyCellValue(value);
	}

	public void setAlign(final Alignment align) {
		impl.setAlign(align);
	}

	public void setCanSort(final Boolean canSort) {
		impl.setCanSort(canSort);
	}

	public void setSummaryFunction(final SummaryFunctionType fct) {
		impl.setSummaryFunction(fct);
	}

	public void setShowGridSummary(Boolean value) {
		impl.setShowGridSummary(value);
	}

	public void setShowGroupSummary(Boolean value) {
		impl.setShowGroupSummary(value);
	}

	public static boolean isDataRecord(final Record record) {
		return	record != null &&
				!JSHelper.isAttribute(record.getJsObj(), "IsGridSummary") &&
				!JSHelper.isAttribute(record.getJsObj(), "isGroupSummary") &&
				!JSHelper.isAttribute(record.getJsObj(), "isSeparator");
	}

}
