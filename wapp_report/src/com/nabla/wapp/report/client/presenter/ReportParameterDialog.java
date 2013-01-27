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
package com.nabla.wapp.report.client.presenter;

import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.report.client.ui.ParameterWizardPageUi;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterList;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;
import com.nabla.wapp.shared.slot.ISlot;


public class ReportParameterDialog extends AbstractWizardPresenter<IWizardDisplay> {

	private final ParameterList		parameters;
	private final ParameterValueList	values;
	private final ISlot				onSuccess;
/*
	public static void run(final IWizardDisplay ui, final ParameterList parameters, final ParameterValueList parameterValues, final ISlot onSuccess) {
		final ParameterBinderList binders = new ParameterBinderList(parameters, parameterValues);
		final ValuesManager values = new ValuesManager();
		values.setDataSource(new ParameterModel(binders));
		final Set<String> parameterNames = new HashSet<String>();
		binders.getNeedDefaultValue(parameterNames);
		if (parameterNames.isEmpty()) {
			values.editNewRecord();
			new ReportParameterDialog(ui, values, parameters, binders, onSuccess).revealDisplay();
		} else {
			Application.getInstance().getDispatcher().execute(new GetFormDefaultValues(IReport.PRINT_REPORT_PREFERENCE_GROUP), new AsyncGetDefaultValuesCallback() {

				@Override
				public void onFailure(Throwable caught) {
					log.log(Level.WARNING, "fail to load '" + IReport.PRINT_REPORT_PREFERENCE_GROUP + "' default values", caught);
					values.editNewRecord();
					new ReportParameterDialog(ui, values, parameters, binders, onSuccess).revealDisplay();
				}

				@Override
				public void onDefaultValues(Map lastUsedValues) {
					if (lastUsedValues != null)
						values.editNewRecord(lastUsedValues);
					else {
						log.fine("no default values");
						values.editNewRecord();
					}
					new ReportParameterDialog(ui, values, parameters, binders, onSuccess).revealDisplay();
				}
			});
		}
	}
*/

	public ReportParameterDialog(final IWizardDisplay ui, final ParameterList parameters, final ParameterValueList values, final ISlot onSuccess) {
		super(ui);
		this.parameters = parameters;
		this.values = values;
		this.onSuccess = onSuccess;
	}

	@Override
	public void bind() {
		super.bind();
		displayPage(0);
	}

	private void displayPage(int pageIndex) {
		final ParameterGroup reportParameters = parameters.get(pageIndex++);
		final ParameterWizardPageUi page = new ParameterWizardPageUi(reportParameters, values);
		if (pageIndex == parameters.size()) {
			displayFinishPage(page, new ISlot() {
				@Override
				public void invoke() {
					page.getValues(values);
					onSuccess.invoke();
					getDisplay().hide();
				}
			});
		} else {
			final Integer nextPageIndex = pageIndex;
			displayNextPage(page, new ISlot() {
				@Override
				public void invoke() {
					page.getValues(values);
					displayPage(nextPageIndex);
				}
			});
		}
	}

}
