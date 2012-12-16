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

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.eclipse.birt.report.engine.api.IReportRunnable;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.report.shared.command.GetBuiltInReport;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IntegerResult;

/**
 * @author nabla
 *
 */
public class GetBuiltInReportHandler extends AbstractHandler<GetBuiltInReport, IntegerResult> {

	private final ReportManager	reportManager;

	@Inject
	public GetBuiltInReportHandler(final ReportManager reportManager) {
		super(true);
		this.reportManager = reportManager;
	}

	@Override
	public IntegerResult execute(final GetBuiltInReport cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final IReportRunnable template = reportManager.openReportTemplate(cmd.getName(), ctx);
		final InputStream report = reportManager.createReport(template, ctx.getReadConnection(), cmd.getParameters(), cmd.getFormat());
		try {
			return new IntegerResult(reportManager.saveReport(ctx.getWriteConnection(), template.getReportName(), report, cmd.getFormat(), cmd.getOutputAsFile()));
		} finally {
			try {
				report.close();
			} catch (IOException e) {
				Util.throwInternalErrorException(e);
			}
		}
	}

}
