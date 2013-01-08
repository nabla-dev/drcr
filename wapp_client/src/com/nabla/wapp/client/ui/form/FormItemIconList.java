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
package com.nabla.wapp.client.ui.form;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;


public class FormItemIconList extends FormItemExtensionList implements IFormItemExtensionList {

	private final List<FormItemIcon>	children = new LinkedList<FormItemIcon>();

	@Override
	public void onCreate(final FormItem item) {
		Assert.argumentNotNull(item);

		if (!children.isEmpty()) {
			item.setIcons(children.toArray(new FormItemIcon[0]));
			children.clear();
		}
	}

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);

		if (w instanceof IFormItemIcon) {
			children.add(((IFormItemIcon)w).getIcon());
		} else {
			super.add(w);
		}
	}

}
