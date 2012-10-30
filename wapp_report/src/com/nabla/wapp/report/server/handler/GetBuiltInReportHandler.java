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

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.IReportFolder;
import com.nabla.wapp.report.server.ReportFile;
import com.nabla.wapp.report.shared.command.GetBuiltInReport;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.auth.AccessDeniedException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IntegerResult;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
public class GetBuiltInReportHandler extends AbstractHandler<GetBuiltInReport, IntegerResult> {

	private final String	reportFolder;

	@Inject
	public GetBuiltInReportHandler(@IReportFolder final String reportFolder) {
		super(true);
		this.reportFolder = reportFolder;
	}

	@Override
	public IntegerResult execute(final GetBuiltInReport cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT *" +
" FROM report INNER JOIN user_role ON report.role_id=user_role.role_id" +
" WHERE user_role.user_id=? AND report.internal_name=?;",
ctx.getUserId(), cmd.getName());
		try {
			final ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new AccessDeniedException();
			final File reportFile = new ReportFile(reportFolder, rs.getString("template")).generate(cmd.getParameters(), ctx.getReadConnection());
			try {
				final Integer id = Database.addRecord(ctx.getWriteConnection(),
"INSERT INTO export (name,format,output_as_file,report) VALUES(?,?,?,?);",
rs.getString("name"), cmd.getFormat().toString(), cmd.getOutputAsFile(), reportFile.getAbsolutePath());
				if (id == null) {
					reportFile.delete();
					throw new InternalErrorException("failed to save report to database");
				}
				return new IntegerResult(id);
			} catch (SQLException e) {
				reportFile.delete();
				throw e;
			}
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

}
