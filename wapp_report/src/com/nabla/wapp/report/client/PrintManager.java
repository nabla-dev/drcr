/**
* Copyright 2011 nabla
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.BasicApplication;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.mvp.IPresenter;
import com.nabla.wapp.client.print.IPrintCommandSet;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.report.client.presenter.ReportParameterDialog;
import com.nabla.wapp.report.shared.IReportParameterValue;
import com.nabla.wapp.report.shared.ReportParameter;
import com.nabla.wapp.report.shared.ReportParameterValueList;
import com.nabla.wapp.report.shared.ReportResult;
import com.nabla.wapp.report.shared.SimpleReportResult;
import com.nabla.wapp.report.shared.command.GetBuiltInReport;
import com.nabla.wapp.report.shared.command.GetReport;
import com.nabla.wapp.report.shared.command.GetSimpleReport;
import com.nabla.wapp.shared.dispatch.IntegerResult;
import com.nabla.wapp.shared.print.ReportFormats;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;

/**
 * @author nabla
 *
 */
@Singleton
public class PrintManager {

	public interface IParameterGetter {
		IReportParameterValue getParameter();
	}

	public interface IReportGetter extends IParameterGetter {
		Set<Integer> getReportIds();
	}

	private static final Logger							logger = LoggerFactory.getLog(PrintManager.class);
	@Inject private IDispatchAsync						server;
	@Inject private IReportParameterBinderFactory		parameterBinderFactory;
	@Inject private ReportParameterDialog.IFactory		parameterDialogFactory;
	private final String								reportUrlFormat;

	public PrintManager() {
		Assert.unique(PrintManager.class);
		reportUrlFormat = GWT.getModuleBaseURL() + "export?id=";
	}

	public <BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bind(final IPrintCommandSet commands, final IPresenter presenter, final BuiltInReportsType builtInReport) {
		bind(commands, presenter, builtInReport, (IParameterGetter)null);
	}

	public <BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bind(final IPrintCommandSet commands, final IPresenter presenter, final BuiltInReportsType builtInReport, final IReportParameterValue parameter) {
		if (parameter == null)
			bind(commands, presenter, builtInReport, (IParameterGetter)null);
		else
			bind(commands, presenter, builtInReport, new IParameterGetter() {
				@Override
				public IReportParameterValue getParameter() {
					return parameter;
				}
			});
	}

	public <BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bind(final IPrintCommandSet commands, final IPresenter presenter, final BuiltInReportsType builtInReport, final IParameterGetter parameterGetter) {
		Assert.argumentNotNull(commands);

		bindBuiltIn(commands.print(), builtInReport, ReportFormats.PDF, false, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsCSV(), builtInReport, ReportFormats.CSV, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsTXT(), builtInReport, ReportFormats.TXT, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsRTF(), builtInReport, ReportFormats.RTF, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsXML(), builtInReport, ReportFormats.XML, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsXLS(), builtInReport, ReportFormats.XLS, true, parameterGetter, presenter);
		bindBuiltIn(commands.exportAsPDF(), builtInReport, ReportFormats.PDF, true, parameterGetter, presenter);
	}

	private	<BuiltInReportsType extends Enum<BuiltInReportsType>>
	void bindBuiltIn(final Command command, final BuiltInReportsType builtInReport, final ReportFormats format, final Boolean outputAsFile, final IParameterGetter parameterGetter, final IPresenter presenter) {
		Assert.argumentNotNull(presenter);
		Assert.argumentNotNull(builtInReport);

		presenter.registerSlot(command, new ISlot() {
			@Override
			public void invoke() {
				server.execute(new GetBuiltInReport(builtInReport.toString(), format, outputAsFile, (parameterGetter != null) ? parameterGetter.getParameter() : null), onCreateBuiltInReport);
			}
		});
	}

	private final AsyncCallback<IntegerResult> onCreateBuiltInReport = new AsyncCallback<IntegerResult>() {
		@Override
		public void onFailure(Throwable caught) {
			logger.log(Level.WARNING, "fail to get report ID", caught);
			Application.getInstance().getMessageBox().error(caught);
		}

		@Override
		public void onSuccess(IntegerResult result) {
			Assert.argumentNotNull(result);

			displayReport(result.get());
		}
	};

	public void bind(final IPrintCommandSet commands, final IPresenter presenter, IReportGetter reportGetter) {
		Assert.argumentNotNull(commands);

		bind(commands.print(), ReportFormats.PDF, false, presenter, reportGetter);
		bind(commands.exportAsCSV(), ReportFormats.CSV, true, presenter, reportGetter);
		bind(commands.exportAsTXT(), ReportFormats.TXT, true, presenter, reportGetter);
		bind(commands.exportAsRTF(), ReportFormats.RTF, true, presenter, reportGetter);
		bind(commands.exportAsXML(), ReportFormats.XML, true, presenter, reportGetter);
		bind(commands.exportAsXLS(), ReportFormats.XLS, true, presenter, reportGetter);
		bind(commands.exportAsPDF(), ReportFormats.PDF, true, presenter, reportGetter);
	}

	private void bind(final Command command, final ReportFormats format, final Boolean outputAsFile, final IPresenter presenter, final IReportGetter reportGetter) {
		Assert.argumentNotNull(reportGetter);
		Assert.argumentNotNull(presenter);

		presenter.registerSlot(command, new ISlot() {
			@Override
			public void invoke() {
				print(reportGetter.getReportIds(), reportGetter.getParameter(), format, outputAsFile);
			}
		});
	}

	public void print(final Set<Integer> reportIds, final IReportParameterValue defaultParameter, final ReportFormats format, final Boolean outputAsFile) {
		if (reportIds.isEmpty()) {
			Application.getInstance().getMessageBox().error("No report selected!");
			return;
		}
		server.execute(new GetSimpleReport(reportIds, defaultParameter, format, outputAsFile), new AsyncCallback<SimpleReportResult>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.WARNING, "fail to get list of report IDs to display", caught);
				BasicApplication.getInstance().getMessageBox().error(caught);
			}

			@Override
			public void onSuccess(final SimpleReportResult result) {
				Assert.argumentNotNull(result);
				if (result.needUserInput()) {
					final List<IReportParameterBinder> parameterBinders = new LinkedList<IReportParameterBinder>();
					final Map<String, Object> defaultParameterValues = new HashMap<String, Object>();
					if (defaultParameter != null)
						defaultParameter.addToMap(defaultParameterValues);
					for (ReportParameter e : result.getParameters()) {
						IReportParameterBinder binder = parameterBinderFactory.create(e, defaultParameterValues);
						Assert.notNull(binder);
						parameterBinders.add(binder);
					}
					ReportParameterDialog dlg = parameterDialogFactory.get(parameterBinders);
					dlg.getSubmitSlots().connect(new ISlot1<ReportParameterValueList>() {
						@Override
						public void invoke(ReportParameterValueList parameters) {
							Assert.argumentNotNull(parameters);

							if (defaultParameter != null)
								parameters.add(defaultParameter);
							server.execute(new GetReport(reportIds, format, outputAsFile, parameters), onCreateReport);
						}
					});
					dlg.revealDisplay();
				} else
					displayReports(result.getReportIds());
			}
		});
	}

	private final AsyncCallback<ReportResult> onCreateReport = new AsyncCallback<ReportResult>() {
		@Override
		public void onFailure(Throwable caught) {
			logger.log(Level.WARNING, "fail to get list of report IDs to display", caught);
			Application.getInstance().getMessageBox().error(caught);
		}

		@Override
		public void onSuccess(ReportResult result) {
			Assert.argumentNotNull(result);

			displayReports(result.getReportIds());
		}
	};

	public void print(final Set<Integer> reportIds, final ReportParameterValueList defaultParameters) {
		print(reportIds, defaultParameters, ReportFormats.PDF, false);
	}

	public void print(final Set<Integer> reportIds) {
		print(reportIds, null);
	}

	public void print(final Integer reportId, final ReportParameterValueList defaultParameters, final ReportFormats format, final Boolean outputAsFile) {
		final Set<Integer> reportIds = new HashSet<Integer>();
		reportIds.add(reportId);
		print(reportIds, defaultParameters, format, outputAsFile);
	}

	public void print(final Integer reportId, final ReportFormats format, final Boolean outputAsFile) {
		print(reportId, null, format, outputAsFile);
	}

	public void print(final Integer reportId, final ReportParameterValueList defaultParameters) {
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
			logger.fine("report= " + reportUrl);
			Window.open(reportUrl, "_blank", null);
		}
	}

}
