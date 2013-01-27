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
package com.nabla.wapp.report.client.ui;

import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.client.mvp.binder.BindedWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPage;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.client.model.ParameterModel;
import com.nabla.wapp.report.client.parameter.ParameterBinderList;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;


public class ParameterWizardPageUi extends BindedWizardPageDisplay<WizardPage> implements IWizardPageDisplay {

	private final Form 					form = new Form();
	private final ParameterBinderList		binders;

	public ParameterWizardPageUi(final ParameterGroup reportParameters, final ParameterValueList values) {
		super();
		binders = new ParameterBinderList(reportParameters, values);
		form.setModel(new ParameterModel(binders));
		binders.createFormItem(form);
		impl = new WizardPage();
		impl.setTitle(reportParameters.getPrompt());
		impl.add(form);
/*
		final Integer reportId = 1;
		form.editNewRecordWithDefault(new GetFormDefaultValues(reportId, IReport.PRINT_REPORT_PREFERENCE_GROUP));*/
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public boolean hasErrors() {
		return form.hasErrors();
	}

	public void getValues(final ParameterValueList parameterValues) {
		binders.getValue(form, parameterValues);
	}
}
