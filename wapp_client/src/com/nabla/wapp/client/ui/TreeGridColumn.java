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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * @author nabla
 *
 */
public class TreeGridColumn extends Widget {

	protected final TreeGridField	impl = new TreeGridField();

	TreeGridField getField(@SuppressWarnings("unused") final TreeGrid parent) {
		if (impl.getName() == null)
			impl.setName(SC.generateID());
		return impl;
	}

	@Override
	public void setWidth(final String w) {
		impl.setWidth(w);
	}

	public void setName(final String name) {
		impl.setName(name);
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

	public void setShowGridSummary(Boolean value) {
		impl.setShowGridSummary(value);
	}

	public void setShowGroupSummary(Boolean value) {
		impl.setShowGroupSummary(value);
	}

}
