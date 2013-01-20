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
import java.util.Map;
import java.util.Set;

import com.nabla.wapp.client.model.field.EnumField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.ui.form.Control;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.IParameterValue;
import com.nabla.wapp.report.shared.parameter.IntegerComboBoxParameter;
import com.nabla.wapp.report.shared.parameter.IntegerParameterValue;
import com.nabla.wapp.shared.general.Nullable;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.ValuesManager;

public class IntegerComboBoxParameterBinder implements IParameterBinder {

	private final IntegerComboBoxParameter		parameter;

	public IntegerComboBoxParameterBinder(final IntegerComboBoxParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public boolean createModelField(List<DataSourceField> fields) {
		final EnumField field = new EnumField(parameter.getName(), FieldAttributes.REQUIRED);
		field.setValueMap(parameter.getValueMap());
		fields.add(field);
		return true;
	}

	@Override
	public boolean createFormItem(Form form, @SuppressWarnings("unused") ParameterBinderList binders) {
		final Control ctrl = new Control();
		ctrl.setName(parameter.getName());
		ctrl.setText(parameter.getPrompt());
		form.add(ctrl);
		return false;
	}

	@Override
	public void getNeedDefaultValue(Set<String> parameterNames) {
		parameterNames.add(parameter.getName());
	}

	@Override
	@Nullable
	public Map<String, String> getValue(ValuesManager manager, 	List<IParameterValue> values) {
		final String value = (String) manager.getValue(parameter.getName());
		values.add(new IntegerParameterValue(parameter.getName(), Integer.valueOf(value)));
		return null;
	}
}
