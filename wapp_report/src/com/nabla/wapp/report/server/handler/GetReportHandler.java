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

import com.google.inject.Inject;
import com.nabla.wapp.report.server.Report;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.report.server.ReportTemplate;
import com.nabla.wapp.report.server.ReportTemplateList;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.ReportResult;
import com.nabla.wapp.report.shared.command.GetReport;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.print.ReportFormats;


public class GetReportHandler extends AbstractHandler<GetReport, ReportResult> {

	private final ReportManager	reportManager;

	@Inject
	public GetReportHandler(final ReportManager reportManager) {
		super(true);
		this.reportManager = reportManager;
	}

	@Override
	public ReportResult execute(final GetReport cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ReportTemplateList templates = reportManager.open(cmd.getReportIds(), ctx);
		final Map<String, Object> parameters = cmd.getParameters().toMap();
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			final ReportResult rslt = new ReportResult();
			if (templates.size() > 1 && cmd.getFormat() == ReportFormats.PDF) {
				final Report report = templates.mergeToPdf(ctx.getReadConnection(), parameters, ctx.getLocale());
				try {
					rslt.add(report.save(conn, cmd.getOutputAsFile(), ctx.getSessionId()));
				} finally {
					report.close();
				}
			} else {
				for (ReportTemplate template : templates) {
					final Report report = template.generate(ctx.getReadConnection(), parameters, cmd.getFormat(), ctx.getLocale());
					try {
						rslt.add(report.save(conn, cmd.getOutputAsFile(), ctx.getSessionId()));
					} finally {
						report.close();
					}
				}
			}
			for (Map.Entry<String, String> parameter : cmd.getParameters().toStringMap().entrySet())
				UserPreference.save(ctx, null, IReport.PRINT_REPORT_PREFERENCE_GROUP, parameter.getKey(), parameter.getValue());
			guard.setSuccess();
			return rslt;
		} finally {
			guard.close();
		}
	}

}
