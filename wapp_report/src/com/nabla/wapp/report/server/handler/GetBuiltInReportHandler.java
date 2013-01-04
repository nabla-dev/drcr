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

import java.sql.SQLException;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.Report;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.report.server.ReportTemplate;
import com.nabla.wapp.report.shared.command.GetBuiltInReport;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.dispatch.AbstractHandler;
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
		final ReportTemplate template = reportManager.open(cmd.getName(), ctx);
		final Report report = template.generate(ctx.getReadConnection(), cmd.getParameters(), cmd.getFormat(), ctx.getLocale());
		try {
			return new IntegerResult(report.save(ctx.getWriteConnection(), cmd.getOutputAsFile()));
		} finally {
			report.close();
		}
	}

}
