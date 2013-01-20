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
package com.nabla.wapp.report.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.IApplication;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.mvp.IPresenter;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.report.client.presenter.ReportParameterDialog;
import com.nabla.wapp.report.client.ui.IParameterWizardDisplayFactory;
import com.nabla.wapp.report.client.ui.Resource;
import com.nabla.wapp.report.shared.ReportResult;
import com.nabla.wapp.report.shared.SimpleReportResult;
import com.nabla.wapp.report.shared.command.GetBuiltInReport;
import com.nabla.wapp.report.shared.command.GetReport;
import com.nabla.wapp.report.shared.command.GetSimpleReport;
import com.nabla.wapp.report.shared.parameter.IParameterValue;
import com.nabla.wapp.report.shared.parameter.ReportParameterValueList;
import com.nabla.wapp.shared.dispatch.IntegerResult;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.print.ReportFormats;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;


public class PrintManager {

	private static final Logger					log = LoggerFactory.getLog(PrintManager.class);
	private final IParameterWizardDisplayFactory	parameterDisplayFactory;
	private final String							reportUrlFormat;

	public PrintManager(final IParameterWizardDisplayFactory parameterDisplayFactory) {
		this.parameterDisplayFactory = parameterDisplayFactory;
		reportUrlFormat = GWT.getModuleBaseURL() + "export?id=";
	}

	public <BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bind(final IPrintCommandSet commands, final IPresenter presenter, final BuiltInReportsType builtInReport) {
		bind(commands, presenter, builtInReport, (IParameterGetter)null);
	}

	public <BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bind(final IPrintCommandSet commands, final IPresenter presenter, final BuiltInReportsType builtInReport, @Nullable final IParameterValue parameter) {
		if (parameter == null)
			bind(commands, presenter, builtInReport, (IParameterGetter)null);
		else
			bind(commands, presenter, builtInReport, new IParameterGetter() {
				@Override
				public IParameterValue getParameter() {
					return parameter;
				}
			});
	}

	public <BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bind(final IPrintCommandSet commands, final IPresenter presenter, final BuiltInReportsType builtInReport, final IParameterGetter parameterGetter) {
		bindBuiltIn(commands.print(), builtInReport, ReportFormats.PDF, false, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsCSV(), builtInReport, ReportFormats.CSV, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsWORD(), builtInReport, ReportFormats.DOC, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsXML(), builtInReport, ReportFormats.XML, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsXLS(), builtInReport, ReportFormats.XLS, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsPDF(), builtInReport, ReportFormats.PDF, true, parameterGetter, presenter);
	}

	public void bind(final IPrintCommandSet commands, final IPresenter presenter, IReportGetter reportGetter) {
		bind(commands.print(), ReportFormats.PDF, false, presenter, reportGetter);
		bind(commands.exportAsCSV(), ReportFormats.CSV, true, presenter, reportGetter);
		bind(commands.exportAsWORD(), ReportFormats.DOC, true, presenter, reportGetter);
		bind(commands.exportAsXML(), ReportFormats.XML, true, presenter, reportGetter);
		bind(commands.exportAsXLS(), ReportFormats.XLS, true, presenter, reportGetter);
		bind(commands.exportAsPDF(), ReportFormats.PDF, true, presenter, reportGetter);
	}

	public 	<BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bindBuiltIn(final Command command, final BuiltInReportsType builtInReport, final ReportFormats format, final Boolean outputAsFile, final IParameterGetter parameterGetter, final IPresenter presenter) {
		presenter.registerSlot(command, new ISlot() {
			@Override
			public void invoke() {
				Application.getInstance().getDispatcher().execute(new GetBuiltInReport(builtInReport.toString(), format, outputAsFile, (parameterGetter != null) ? parameterGetter.getParameter() : null), onCreateBuiltInReport);
			}
		});
	}

	private final AsyncCallback<IntegerResult> onCreateBuiltInReport = new AsyncCallback<IntegerResult>() {
		@Override
		public void onFailure(Throwable caught) {
			log.log(Level.WARNING, "fail to get report ID", caught);
			Application.getInstance().getMessageBox().error(caught);
		}

		@Override
		public void onSuccess(IntegerResult result) {
			if (result != null)
				displayReport(result.get());
		}
	};

	private void bind(final Command command, final ReportFormats format, final Boolean outputAsFile, final IPresenter presenter, final IReportGetter reportGetter) {
		presenter.registerSlot(command, new ISlot() {
			@Override
			public void invoke() {
				print(reportGetter.getReportIds(), reportGetter.getParameter(), format, outputAsFile);
			}
		});
	}

	@SuppressWarnings("static-access")
	public void print(final Set<Integer> reportIds, @Nullable final IParameterValue defaultParameter, final ReportFormats format, final Boolean outputAsFile) {
		final IApplication app = Application.getInstance();
		if (reportIds.isEmpty()) {
			app.getMessageBox().error(Resource.instance.strings.noReportSelected());
			return;
		}
		app.getDispatcher().execute(new GetSimpleReport(reportIds, defaultParameter, format, outputAsFile), new AsyncCallback<SimpleReportResult>() {
			@Override
			public void onFailure(Throwable caught) {
				log.log(Level.WARNING, "fail to get list of report IDs to display", caught);
				app.getMessageBox().error(caught);
			}

			@Override
			public void onSuccess(final SimpleReportResult result) {
				Assert.argumentNotNull(result);
				if (result.needUserInput()) {
					final Map<String, Object> defaultValues = new HashMap<String, Object>();
					if (defaultParameter != null)
						defaultParameter.addToMap(defaultValues);
					ReportParameterDialog.run(parameterDisplayFactory.get(), result.getParameters(), defaultValues, new ISlot1<ReportParameterValueList>() {
						@Override
						public void invoke(ReportParameterValueList parameters) {
							if (defaultParameter != null)
								parameters.add(defaultParameter);
							app.getDispatcher().execute(new GetReport(reportIds, format, outputAsFile, parameters), onCreateReport);
						}
					});
				} else
					displayReports(result);
			}
		});
	}

	private final AsyncCallback<ReportResult> onCreateReport = new AsyncCallback<ReportResult>() {
		@Override
		public void onFailure(Throwable caught) {
			log.log(Level.WARNING, "fail to get list of report IDs to display", caught);
			Application.getInstance().getMessageBox().error(caught);
		}

		@Override
		public void onSuccess(ReportResult result) {
			if (result != null)
				displayReports(result);
		}
	};

	public void print(final Set<Integer> reportIds, @Nullable final ReportParameterValueList defaultParameters) {
		print(reportIds, defaultParameters, ReportFormats.PDF, false);
	}

	public void print(final Set<Integer> reportIds) {
		print(reportIds, null);
	}

	public void print(final Integer reportId, @Nullable final ReportParameterValueList defaultParameters, final ReportFormats format, final Boolean outputAsFile) {
		final Set<Integer> reportIds = new HashSet<Integer>();
		reportIds.add(reportId);
		print(reportIds, defaultParameters, format, outputAsFile);
	}

	public void print(final Integer reportId, final ReportFormats format, final Boolean outputAsFile) {
		print(reportId, null, format, outputAsFile);
	}

	public void print(final Integer reportId, @Nullable final ReportParameterValueList defaultParameters) {
		print(reportId, defaultParameters, ReportFormats.PDF, false);
	}

	public void print(final Integer reportId) {
		print(reportId, null);
	}

	private void displayReports(final Set<Integer> reportIds) {
		for (Integer id : reportIds)
			displayReport(id);
	}

	private void displayReport(final Integer reportId) {
		// open window and display report
		if (reportId != null) {
			final String reportUrl = reportUrlFormat + reportId.toString();
			log.fine("report= " + reportUrl);
			Window.open(reportUrl, "_blank", null);
		}
	}

}
