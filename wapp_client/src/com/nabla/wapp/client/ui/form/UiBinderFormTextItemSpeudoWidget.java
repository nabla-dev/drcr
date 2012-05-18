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

import com.google.gwt.user.client.ui.HasText;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.ui.TitleDecoder;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * @author nabla
 *
 */
public class UiBinderFormTextItemSpeudoWidget<C extends FormItem> extends UiBinderFormItemSpeudoWidget<C> implements HasText {

	private FormItemValueFormatter	defaultValueFormatter = null;

	protected UiBinderFormTextItemSpeudoWidget(C impl, boolean inputField) {
		super(impl, inputField);
		impl.setWrapTitle(false);
	}

	@Override
	public String getText() {
		return impl.getTitle();
	}

	@Override
	public void setText(String text) {
		new TitleDecoder(text).apply(impl);
	}

	public void setValue(String value) {
		impl.setValue(value);
	}

	public void setValueFormatter(final FormItemValueFormatter formatter) {
		defaultValueFormatter = formatter;
	}

	@Override
	public FormItem getItem(final Model model) {
		final FormItem ret = super.getItem(model);
		if (isReadOnly() && defaultValueFormatter != null)
			impl.setEditorValueFormatter(defaultValueFormatter);
		return ret;
	}

}
