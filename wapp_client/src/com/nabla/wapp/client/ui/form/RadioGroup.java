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

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.ui.TitleDecoder;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;


/**
 * @author nabla
 *
 */
public class RadioGroup extends UiBinderFormItemSpeudoWidgetList<RadioGroupItem> {

	private final LinkedHashMap<String, String>	values = new LinkedHashMap<String, String>();

	public RadioGroup() {
		super(new RadioGroupItem(), true);
		impl.setShowTitle(false);
		impl.setWrapTitle(false);
		impl.setWrap(false);
	}

	public void setVertical(Boolean vertical) {
		impl.setVertical(vertical);
	}

	public void setDefaultValue(String value) {
		impl.setDefaultValue(value);
	}

	@Override
	public void setTitle(final String title) {
		new TitleDecoder(title).apply(impl);
	}

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);

		if (w instanceof RadioItem) {
			final RadioItem item = (RadioItem)w;
			values.put(item.getKey(), item.getText());
		} else
			super.add(w);
	}

	@Override
	public FormItem getItem(final Model model) {
		if (!values.isEmpty()) {
			impl.setValueMap(values);
		}
		return super.getItem(model);
	}
}
