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

import java.util.HashMap;
import java.util.Map;

import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.IParameter;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterList;

public class ParameterBinderList extends HashMap<String, IParameterBinder> {

	private static final ParameterBinderFactoryRegister	factories = new ParameterBinderFactoryRegister();

	private static final long serialVersionUID = 1L;

	public ParameterBinderList(final ParameterList parameters, final Map<String, Object> defaultValues) {
		for (ParameterGroup report : parameters)
			constructor(report, defaultValues);
	}

	public void createFormItem(final Form form, final ParameterGroup reportParameters) {
		for (IParameter parameter : reportParameters)
			get(parameter.getName()).createFormItem(form, this);
	}

	private void constructor(final ParameterGroup parameters, final Map<String, Object> defaultValues) {
		for (IParameter parameter : parameters) {
			put(parameter.getName(), factories.create(parameter, defaultValues));
			if (parameter instanceof ParameterGroup)
				constructor((ParameterGroup) parameter, defaultValues);
		}
	}
}
