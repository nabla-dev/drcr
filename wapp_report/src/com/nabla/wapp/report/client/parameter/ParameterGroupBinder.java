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

import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;

public class ParameterGroupBinder extends ParameterBinderList implements IParameterBinder {

	private static final long serialVersionUID = 1L;

	private final ParameterGroup		parameter;

	public ParameterGroupBinder(final ParameterGroup parameter, final ParameterValueList parameterValues) {
		super(parameter, parameterValues);
		this.parameter = parameter;
	}

	@Override
	public void createFormItem(Form form) {
		final Form ctrl = new Form();
		ctrl.setIsGroup(true);
		ctrl.setGroupTitle(parameter.getPrompt());
		ctrl.setWidth("100%");
		super.createFormItem(ctrl);
		form.add(ctrl);
	}
/*
	@Override
	public void getNeedDefaultValue(final Set<String> parameterNames) {
		binders.getNeedDefaultValue(parameterNames);
	}

	@Override @Nullable
	public Map<String, String> getValue(ValuesManager manager, 	List<IParameterValue> values) {
		return binders.getValue(manager, values);
	}
*/
}
