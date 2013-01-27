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

import java.util.LinkedList;
import java.util.List;

import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.IParameter;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;
import com.smartgwt.client.data.DataSourceField;

public class ParameterBinderList extends LinkedList<IParameterBinder> {

	private static final ParameterBinderFactoryRegister	factories = new ParameterBinderFactoryRegister();

	private static final long serialVersionUID = 1L;

	public ParameterBinderList(final ParameterGroup parameter, final ParameterValueList parameterValues) {
		for (IParameter e : parameter)
			add(factories.create(e, parameterValues));
	}

	public void createModelField(final List<DataSourceField> fields) {
		for (IParameterBinder e : this)
			e.createModelField(fields);
	}

	public void createFormItem(final Form form) {
		for (IParameterBinder e : this)
			e.createFormItem(form);
	}
/*
	@Override
	public void getNeedDefaultValue(Set<String> parameterNames) {
		for (IParameterBinder e : this)
			e.getNeedDefaultValue(parameterNames);
	}*/

	public void getValue(final Form form, final ParameterValueList parameterValues) {
		for (IParameterBinder e : this)
			e.getValue(form, parameterValues);
	}

}
