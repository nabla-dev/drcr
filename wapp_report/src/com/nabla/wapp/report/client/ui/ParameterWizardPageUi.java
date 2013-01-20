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
import com.nabla.wapp.client.mvp.binder.BindedBasicWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPage;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.client.parameter.ParameterBinderList;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.smartgwt.client.widgets.form.ValuesManager;


public class ParameterWizardPageUi extends BindedBasicWizardPageDisplay implements IWizardPageDisplay {

	public ParameterWizardPageUi(final ValuesManager model, final ParameterBinderList binders, final ParameterGroup reportParameters) {
		super(model);
		impl = new WizardPage();
		impl.setTitle(reportParameters.getPrompt());
		form = new Form();
		form.setValuesManager(model);
		binders.createFormItem(form, reportParameters);
		impl.add(form);
	}

}
