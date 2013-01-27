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

import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;
import com.smartgwt.client.data.DataSourceField;

/**
 * The <code></code> object is used to
 *
 */
public interface IParameterBinder {
//	boolean isReadOnly();
	void createModelField(List<DataSourceField> fields);
	void createFormItem(Form form);
//	void onCreate(Form form);
//	void getNeedDefaultValue(Set<String> parameterNames);
	void getValue(Form form, ParameterValueList parameterValues);
}
