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

import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.IParameterValue;
import com.nabla.wapp.shared.general.Nullable;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * The <code></code> object is used to
 *
 */
public interface IParameterBinder {
	boolean createModelField(List<DataSourceField> fields);

	boolean createFormItem(Form form, ParameterBinderList binders);

	void getNeedDefaultValue(Set<String> parameterNames);

	// return: errors if any
	@Nullable Map<String, String> getValue(ValuesManager manager, List<IParameterValue> values);
}
