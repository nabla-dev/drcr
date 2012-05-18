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
package com.nabla.wapp.client.model.field;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.Model;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FilterCriteriaFunction;

/**
 * @author nabla64
 *
 */
public class ComboBoxField extends TextField implements FilterCriteriaFunction {

	private static final String		RESET_TAG_NAME = "reset";

	private Model				model;
	private final ComboBoxItem	editor = new ComboBoxItem();
	// TODO: to be removed when SmartGWT will let us invalidate pick list cache
	private Integer				reset = 0;

	public ComboBoxField(final String name) {
		super(name);
		setEditorType(editor);
	}

	public ComboBoxField(final String name, final Model model, final String valueField, final String displayField, final FieldAttributes... attributes) {
		this(name);
		setModel(model, valueField, displayField);
		FieldAttributes.applyAll(this, attributes);
	}

	public ComboBoxField(final String name, final Model model, final String displayField, final FieldAttributes... attributes) {
		this(name);
		setModel(model, displayField);
		FieldAttributes.applyAll(this, attributes);
	}

	public void setModel(final Model model, final String valueField, final String displayField) {
		Assert.argumentNotNull(model);
		Assert.argumentNotNull(valueField);
		Assert.argumentNotNull(displayField);
		Assert.notNull(model.getField(valueField));
		Assert.notNull(model.getField(displayField));

		this.model = model;
		editor.setOptionDataSource(model);
		editor.setValueField(valueField);
		editor.setDisplayField(displayField);
		editor.setSortField(displayField);
		setEditorType(editor);
		// find first TextField that will be used in pick list
		this.setLength(model.getField(displayField).getLength());
	}

	public void setModel(final Model model, final String displayField) {
		setModel(model, displayField, displayField);
	}

	public Model getModel() {
		return model;
	}

	public String getValueField() {
		return editor.getValueField();
	}

	public String getDisplayField() {
		return editor.getDisplayField();
	}

	@Override
	public Criteria getCriteria() {
		return new Criteria(RESET_TAG_NAME, reset.toString());
	}

	public void invalidatePickListCache() {
		++reset;
	}

}
