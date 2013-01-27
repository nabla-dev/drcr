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

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.ui.Resource;
import com.nabla.wapp.client.ui.TitleDecoder;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.fields.FormItem;


public class Control extends Widget implements IFormItemSpeudoWidget, HasText {

	private static final Logger	logger = LoggerFactory.getLog(Control.class);

	private Boolean		readOnly;
	private String		width;
	private String		height;
	private String		name;
	private Integer		colSpan;
	private Boolean		required;
	private Boolean		showTitle;
	private Boolean		validateOnChange;
	private String		text;
	private Boolean		visible;
	private Boolean		enabled;
	private Boolean		shouldSaveValue;
	private Boolean		redrawOnChange;

	public Control() {}

	@Override
	public FormItem getItem(final Model model) {
		Assert.argumentNotNull(model);
		Assert.notNull(name);

		final DataSourceField field = model.getField(name);
		Assert.notNull(field);
		if (required == null) {
			if (readOnly != null && readOnly)
				required = false;
			else
				required = field.getRequired();
		}
		final FormItem impl = ControlTypes.createEditor(field);
		logger.fine("created form item '" + name + "' as " + impl.getType() + " : " + Util.getClassSimpleName(impl.getClass()));
		impl.setName(name);
		impl.setRequired(required);
		impl.setWrapTitle(false);
		if (required) {
		/*	logger.fine("add required hint to " + impl.getType() + " '" + impl.getName() + "'");
			impl.setHint(Resource.strings.requiredHintTag());
			impl.setHintStyle(Resource.bundle.style().formRequiredInfoTag());*/
			impl.setRequiredMessage(Resource.strings.requiredFieldErrorMessage());
		}
		if (width != null)
			impl.setWidth(width);
		if (height != null)
			impl.setHeight(height);
		if (colSpan != null)
			impl.setColSpan(colSpan);
		if (showTitle != null)
			impl.setShowTitle(showTitle);
		if (validateOnChange != null)
			impl.setValidateOnChange(validateOnChange);
		if (redrawOnChange != null)
			impl.setRedrawOnChange(redrawOnChange);
		if (text != null)
			new TitleDecoder(text).apply(impl);
		if (visible != null)
			impl.setVisible(visible);
		if (enabled != null)
			impl.setDisabled(!enabled);
		if (shouldSaveValue != null)
			impl.setShouldSaveValue(shouldSaveValue);
		else {
			Boolean can = FieldAttributes.getCanSave(field);
			if (can != null && !can)
				impl.setShouldSaveValue(false);
		}
		Boolean can = FieldAttributes.getCanEdit(field);
		if ((readOnly != null && readOnly) || (can != null && !can)) {
		//	impl.setDisabled(true);
		//	impl.setShowDisabled(false);
			impl.setCanEdit(false);
		}
		return impl;
	}

	@Override
	public boolean isInputField() {
		return true;
	}

	@Override
	public void setWidth(final String w) {
		width = w;
	}

	@Override
	public void setHeight(final String h) {
		height = h;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setColSpan(final int colSpan) {
		this.colSpan = colSpan;
	}

	public void setReadOnly(final boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public void setShowTitle(final Boolean showTitle) {
		this.showTitle = showTitle;
	}

	public void setValidateOnChange(final boolean validate) {
		validateOnChange = validate;
	}

	public void setShouldSaveValue(final boolean save) {
		shouldSaveValue = save;
	}

	public void setRedrawOnChange(final boolean redraw) {
		redrawOnChange = redraw;
	}

	@Override
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public void setEnabled(boolean value) {
		enabled = value;
	}

	public void setDisabled(boolean value) {
		enabled = !value;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(final String text) {
		this.text = text;
	}

}
