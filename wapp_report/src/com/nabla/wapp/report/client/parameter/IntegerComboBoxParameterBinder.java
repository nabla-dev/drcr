/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.client.parameter;

import java.util.List;

import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.SelectBoxField;
import com.nabla.wapp.client.ui.form.Control;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.client.model.ParameterValueMapModel;
import com.nabla.wapp.report.shared.parameter.IntegerComboBoxParameter;
import com.nabla.wapp.report.shared.parameter.IntegerParameterValue;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;
import com.smartgwt.client.data.DataSourceField;

public class IntegerComboBoxParameterBinder implements IParameterBinder {

	private final IntegerComboBoxParameter		parameter;
	private final boolean						readOnly;
	private final ParameterValueList			values;
	private SelectBoxField						field;

	public IntegerComboBoxParameterBinder(final IntegerComboBoxParameter parameter, final ParameterValueList values) {
		this.parameter = parameter;
		readOnly = values.containsKey(parameter.getName());
		this.values = values;
	}

//	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void createModelField(final List<DataSourceField> fields) {
		field = new SelectBoxField(parameter.getName(),
							new ParameterValueMapModel(parameter.getReportId(), parameter.getName(), values),
							IdField.NAME, "name", FieldAttributes.REQUIRED);
		fields.add(field);
	}

	@Override
	public void createFormItem(final Form form) {
		final Control ctrl = new Control();
		ctrl.setName(parameter.getName());
		ctrl.setText(parameter.getPrompt());
		ctrl.setWidth("100%");
		form.add(ctrl);
	}
/*
	@Override
	public void onCreate(final Form form) {
		if (!isReadOnly()) {
			form.getItem(parameter.getName()).addChangedHandler(new ChangedHandler() {
	            @Override
				public void onChanged(ChangedEvent event) {
	            	updateValue((String) event.getValue());

	            }
	        });
		}
	}

	public void onPreviousCascadingFieldChanged(final Form form) {
		if (!isReadOnly()) {
           	form.clearValue(parameter.getName());
           	field.invalidatePickListCache();
		}
	}

	@Override
	public void getNeedDefaultValue(final Set<String> parameterNames) {
		parameterNames.add(parameter.getName());
	}

	@Override
	public void storeValue(final ValuesManager data) {
		if (!isReadOnly())
			updateValue((String) data.getValue(parameter.getName()));
	}
*/
	@Override
	public void getValue(Form form, ParameterValueList parameterValues) {
		if (!isReadOnly())
			storeValue((String) form.getValue(parameter.getName()), parameterValues);
	}

	private void storeValue(final String value, ParameterValueList parameterValues) {
		if (value == null)
			parameterValues.remove(parameter.getName());
		else
			parameterValues.add(new IntegerParameterValue(parameter.getName(), Integer.valueOf(value)));
	}

}
