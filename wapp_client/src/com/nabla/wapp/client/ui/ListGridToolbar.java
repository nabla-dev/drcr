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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;


/**
 * The <code></code> object is used to
 *
 */
public class ListGridToolbar extends HLayout {

	private ListGrid	list;
	private String		fieldName;

	public ListGridToolbar() {
		setSnapTo("TR");
    	setHeight(22);
    	setWidth(1);
	}

	@Override
	public void add(final Widget w){
		super.add(w);
		if (w instanceof ListGridToolbarButton) {
			final ListGridToolbarButton button = (ListGridToolbarButton)w;
			button.setParent(this);
		} else if (w instanceof ListGridToolbarMenu) {
			final ListGridToolbarMenu button = (ListGridToolbarMenu)w;
			button.setParent(this);
		}
	}

	public void setParent(final ListGrid list) {
		this.list = list;
	}

	public void setField(final String fieldName) {
		this.fieldName = fieldName;
	}

	public String getField() {
		return fieldName;
	}

	public boolean isEmpty() {
		return this.getMembers() == null || this.getMembers().length == 0;
	}

	public int getMemberCount() {
		final Canvas[] children = getMembers();
		return children == null ? 0 : children.length;
	}

	public JavaScriptObject getCurrentRecord() {
		return (list == null) ? null : list.getCurrentRecord().getJsObj();
	}

}
