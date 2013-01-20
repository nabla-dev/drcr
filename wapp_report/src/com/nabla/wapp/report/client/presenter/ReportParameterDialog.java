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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.AsyncGetDefaultValuesCallback;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.report.client.model.ParameterModel;
import com.nabla.wapp.report.client.parameter.IParameterBinder;
import com.nabla.wapp.report.client.parameter.ParameterBinderList;
import com.nabla.wapp.report.client.ui.ParameterWizardPageUi;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterList;
import com.nabla.wapp.report.shared.parameter.ReportParameterValueList;
import com.nabla.wapp.shared.command.GetFormDefaultValues;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.widgets.form.ValuesManager;


public class ReportParameterDialog extends AbstractWizardPresenter<IWizardDisplay> {

	private static final Logger						log = LoggerFactory.getLog(ReportParameterDialog.class);
	private final ParameterList						parameters;
	private final ParameterBinderList					binders;
	private final ValuesManager						values;
	private final ISlot1<ReportParameterValueList>		onSuccess;

	public static void run(final IWizardDisplay ui, final ParameterList parameters, final Map<String, Object> defaultValues, final ISlot1<ReportParameterValueList> onSuccess) {
		final ParameterBinderList binders = new ParameterBinderList(parameters, defaultValues);
		final ValuesManager values = new ValuesManager();
		values.setDataSource(new ParameterModel(binders));
		final Set<String> parameterNames = new HashSet<String>();
		for (IParameterBinder e : binders.values())
			e.getNeedDefaultValue(parameterNames);
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

	ReportParameterDialog(final IWizardDisplay ui, final ValuesManager values, final ParameterList parameters, final ParameterBinderList binders, final ISlot1<ReportParameterValueList> onSuccess) {
		super(ui);
		this.parameters = parameters;
		this.binders = binders;
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
		if (pageIndex == parameters.size()) {
			displayFinishPage(new ParameterWizardPageUi(values, binders, reportParameters), new ISlot() {
				@Override
				public void invoke() {
					final ReportParameterValueList parameterValues = new ReportParameterValueList();
					for (IParameterBinder e : binders.values()) {
						final Map<String, String> errors = e.getValue(values, parameterValues);
						if (errors != null) {
							values.setErrors(errors, true);
							return;
						}
					}
					onSuccess.invoke(parameterValues);
					getDisplay().hide();
				}
			});
		} else {
			final Integer nextPageIndex = pageIndex;
			displayNextPage(new ParameterWizardPageUi(values, binders, reportParameters), new ISlot() {
				@Override
				public void invoke() {
					displayPage(nextPageIndex);
/*					Integer fixedAssetCategoryId = data.getRecord().getCategoryId();
					Application.getInstance().getDispatcher().execute(new GetFixedAssetCategoryDepreciationPeriodRange(fixedAssetCategoryId), new AsyncCallback<DepreciationPeriodRange>() {
						@Override
						public void onFailure(Throwable caught) {
							log.log(Level.WARNING, "failed to get depreciation range. use default range", caught);
							displayAcquisitionPage(new DepreciationPeriodRange(IAsset.DEPRECIATION_PERIOD_CONSTRAINT.getMinValue(), IAsset.DEPRECIATION_PERIOD_CONSTRAINT.getMaxValue()));
						}

						@Override
						public void onSuccess(DepreciationPeriodRange range) {
							log.fine("restricting depreciation range to [" + range.getMin() + ", " + range.getMax() + "]");
							displayAcquisitionPage(range);
						}
					});*/
				}
			});
		}
	}

}
