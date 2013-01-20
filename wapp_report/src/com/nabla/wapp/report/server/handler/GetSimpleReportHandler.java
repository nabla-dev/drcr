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
package com.nabla.wapp.report.server.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.Report;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.report.server.ReportTemplate;
import com.nabla.wapp.report.server.ReportTemplateList;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.SimpleReportResult;
import com.nabla.wapp.report.shared.command.GetSimpleReport;
import com.nabla.wapp.report.shared.parameter.ParameterGroup;
import com.nabla.wapp.report.shared.parameter.ParameterList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.print.ReportFormats;


public class GetSimpleReportHandler extends AbstractHandler<GetSimpleReport, SimpleReportResult> {

	private static final Log		log = LogFactory.getLog(GetSimpleReportHandler.class);
	private final ReportManager	reportManager;

	@Inject
	public GetSimpleReportHandler(final ReportManager reportManager) {
		super(true);
		this.reportManager = reportManager;
	}

	@Override
	public SimpleReportResult execute(final GetSimpleReport cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ReportTemplateList templates = reportManager.open(cmd.getReportIds(), ctx);
		final Map<String, Object> defaultParameterValues = cmd.getParameterValues();
		final ParameterList reportParameters = new ParameterList();
		for (ReportTemplate template : templates) {
			final ParameterGroup parameters = template.getParameters(defaultParameterValues);
			if (parameters.needUserInput())
				reportParameters.add(parameters);
		}
		if (reportParameters.needUserInput()) {
			if (log.isDebugEnabled())
				log.debug("need extra parameters");
			return new SimpleReportResult(reportParameters);
		}
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			final SimpleReportResult rslt = new SimpleReportResult();
			if (templates.size() > 1 && cmd.getFormat() == ReportFormats.PDF) {
				final Report report = templates.mergeToPdf(ctx.getReadConnection(), defaultParameterValues, ctx.getLocale());
				try {
					rslt.add(report.save(conn, cmd.getOutputAsFile(), ctx.getSessionId()));
				} finally {
					report.close();
				}
			} else {
				for (ReportTemplate template : templates) {
					final Report report = template.generate(ctx.getReadConnection(), defaultParameterValues, cmd.getFormat(), ctx.getLocale());
					try {
						rslt.add(report.save(conn, cmd.getOutputAsFile(), ctx.getSessionId()));
					} finally {
						report.close();
					}
				}
			}
			for (Map.Entry<String, String> parameter : cmd.toStringMap().entrySet())
				UserPreference.save(ctx, null, IReport.PRINT_REPORT_PREFERENCE_GROUP, parameter.getKey(), parameter.getValue());
			guard.setSuccess();
			return rslt;
		} finally {
			guard.close();
		}
	}

}
