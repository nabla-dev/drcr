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

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.ui.Resource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * @author nabla
 *
 */
public class UiBinderFormItemSpeudoWidget<C extends FormItem> extends Widget implements IFormItemSpeudoWidget {

	private static final Logger	logger = LoggerFactory.getLog(UiBinderFormItemSpeudoWidget.class);
	protected final C			impl;
	private final boolean		inputField;

	protected UiBinderFormItemSpeudoWidget(final C impl, final boolean inputField) {
		Assert.argumentNotNull(impl);

		this.impl = impl;
		this.inputField = inputField;
		setCanEdit(inputField);
	}

	protected UiBinderFormItemSpeudoWidget(final C impl) {
		Assert.argumentNotNull(impl);

		this.impl = impl;
		this.inputField = false;
	}

	public C getImpl() {
		return impl;
	}

	@Override
	public FormItem getItem(final Model model) {
		if (model != null && !isReadOnly() && !impl.getRequired()) {
			// update item status with associated model field if any
			final DataSourceField field = model.getField(impl.getName());
			if (field.getCanEdit())

			impl.setRequired(field != null && field.getRequired());
		}
		if (impl.getRequired() && !impl.getDisabled()) {
			logger.fine("add required hint to '" + impl.getName() + "'");
			impl.setHint(Resource.strings.requiredHintTag());
			impl.setHintStyle(Resource.bundle.style().formRequiredInfoTag());
			impl.setRequiredMessage(Resource.strings.requiredFieldErrorMessage());
		}
		return impl;
	}

	@Override
	public boolean isInputField() {
		return inputField;
	}

	@Override
	public void setWidth(final String w) {
		impl.setWidth(w);
	}

	@Override
	public void setVisible(boolean visible) {
		impl.setVisible(visible);
	}

	@Override
	public void setHeight(final String h) {
		impl.setHeight(h);
	}

	public void setName(final String name) {
		impl.setName(name);
	}

	public String getName() {
		return impl.getName();
	}

	public void setColSpan(final int colSpan) {
		impl.setColSpan(colSpan);
	}

	public void setStartRow(Boolean startRow) {
		impl.setStartRow(startRow);
	}

	public void setEndRow(Boolean endRow) {
		impl.setEndRow(endRow);
	}

	public void setReadOnly(final boolean readOnly) {
		setCanEdit(!readOnly);
	}

	public boolean isReadOnly() {
		// not available for < SmartGWT 3
		return !getCanEdit();
	}

	public void setRequired(final boolean required) {
		impl.setRequired(required);
	}

	public void setShowTitle(final Boolean showTitle) {
		impl.setShowTitle(showTitle);
	}

	public void setCellStyle(final String style) {
		impl.setCellStyle(style);
	}

	public void setValidateOnChange(boolean validate) {
		impl.setValidateOnChange(validate);
	}

	public void setShouldSaveValue(boolean save) {
		impl.setShouldSaveValue(save);
	}

	public void setDisabled(boolean value) {
		impl.setDisabled(value);
	}

	public void setCanFocus(boolean value) {
		impl.setCanFocus(value);
	}

	private void setCanEdit(boolean value) {
		// not available for < SmartGWT 3
		impl.setDisabled(!value);
		impl.setShowDisabled(false);
	}

	private boolean getCanEdit() {
		// not available for < SmartGWT 3
		return !impl.getDisabled();
	}

}
