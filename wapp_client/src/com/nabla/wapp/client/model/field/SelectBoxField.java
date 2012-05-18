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

import java.util.logging.Logger;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.Model;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.form.fields.FilterCriteriaFunction;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * @author nabla64
 *
 */
public class SelectBoxField extends TextField implements FilterCriteriaFunction {

	private static final Logger		logger = LoggerFactory.getLog(SelectBoxField.class);
	private static final String		RESET_TAG_NAME = "reset";

	private Model				model;
	private final SelectItem	editor = new SelectItem();
	// TODO: to be removed when SmartGWT will let us invalidate pick list cache
	private Integer				reset = 0;
	private Criteria			resetCriteria = new Criteria(RESET_TAG_NAME, reset.toString());

	public SelectBoxField(final String name) {
		super(name);
		setEditorType(editor);
	}

	public SelectBoxField(final String name, final Model model, final String valueField, final String displayField, final FieldAttributes... attributes) {
		this(name);
		setModel(model, valueField, displayField);
		FieldAttributes.applyAll(this, attributes);
	}

	public SelectBoxField(final String name, final Model model, final FieldAttributes... attributes) {
		this(name);
		setModel(model);
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
	//	editor.setPickListFilterCriteriaFunction(this);
		setEditorType(editor);
	}

	public void setModel(final Model model) {
		Assert.argumentNotNull(model);

		this.model = model;
		editor.setOptionDataSource(model);
		setEditorType(editor);
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
		logger.info("SelectBoxField: getCriteria reset = " + reset);
		return resetCriteria;
	}

	public void invalidatePickListCache() {
		++reset;
		resetCriteria = new Criteria(RESET_TAG_NAME, reset.toString());
	}

}
