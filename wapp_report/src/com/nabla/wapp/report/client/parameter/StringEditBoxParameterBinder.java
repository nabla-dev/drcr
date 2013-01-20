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

import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.IParameterValue;
import com.nabla.wapp.report.shared.parameter.StringEditBoxParameter;
import com.nabla.wapp.report.shared.parameter.StringParameterValue;
import com.nabla.wapp.shared.general.Nullable;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * The <code></code> object is used to
 *
 */
public class StringEditBoxParameterBinder extends TSimpleParameterBinder<StringEditBoxParameter, String> {

	public StringEditBoxParameterBinder(final StringEditBoxParameter parameter, final String value) {
		super(parameter, value);
	}

	@Override
	public boolean createModelField(List<DataSourceField> fields) {
		fields.add(new TextField(parameter.getName(), isReadOnly() ? FieldAttributes.READ_ONLY : FieldAttributes.REQUIRED));
		return true;
	}

	@Override
	public boolean createFormItem(Form form, ParameterBinderList binders) {
		super.createFormItem(form, binders);
		if (isReadOnly())
			form.setValue(parameter.getName(), value);
		return true;
	}

	@Override @Nullable
	public Map<String, String> getValue(final ValuesManager manager, final List<IParameterValue> values) {
		final String value = (String) manager.getValue(parameter.getName());
		values.add(new StringParameterValue(parameter.getName(), value));
		return null;
	}

}
