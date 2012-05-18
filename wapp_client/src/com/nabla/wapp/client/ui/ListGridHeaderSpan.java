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

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.smartgwt.client.widgets.grid.HeaderSpan;


/**
 * @author nabla
 *
 */
public class ListGridHeaderSpan extends UiBinderSpeudoWidgetList {

	private final HeaderSpan		impl = new HeaderSpan();
	private List<ListGridColumn>	children = new LinkedList<ListGridColumn>();

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);
		Assert.notNull(children);

		if (w instanceof ListGridColumn)
			children.add((ListGridColumn)w);
		else
			super.add(w);
	}

	@Override
	public void setTitle(final String title) {
		impl.setTitle(title);
	}

	public HeaderSpan getImpl() {
		if (children != null) {
			final List<String> columns = new LinkedList<String>();
			for (ListGridColumn col : children)
				columns.add(col.getName());
			impl.setFields(columns.toArray(new String[0]));
		}
		return impl;
	}

	public List<ListGridColumn> getColumns() {
		return children;
	}

}
