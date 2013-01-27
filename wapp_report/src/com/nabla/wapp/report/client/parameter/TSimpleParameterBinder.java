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

import com.nabla.wapp.client.ui.form.Control;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.parameter.IParameter;

public abstract class TSimpleParameterBinder<P extends IParameter, T> implements IParameterBinder {

	protected final P	parameter;
	protected final T	value;

	public TSimpleParameterBinder(final P parameter, final T value) {
		this.parameter = parameter;
		this.value = value;
	}

	@Override
	public void createFormItem(Form form) {
		final Control ctrl = new Control();
		ctrl.setName(parameter.getName());
		ctrl.setText(parameter.getPrompt());
		ctrl.setWidth("100%");
		form.add(ctrl);
	}
/*
	@Override
	public void onCreate(final Form form) {
		form.getItem(parameter.getName()).addChangedHandler(new ChangedHandler() {
            @Override
			public void onChanged(@SuppressWarnings("unused") ChangedEvent event) {

            }
        });
	}

	@Override
	public void getNeedDefaultValue(Set<String> parameterNames) {
		if (!isReadOnly())
			parameterNames.add(parameter.getName());
	}

	public boolean isReadOnly() {
		return value != null;
	}*/
}
