/**
* Copyright 2010 nabla
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
package com.nabla.wapp.report.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
public class ReportDatabase {

	static private final Log	log = LogFactory.getLog(ReportDatabase.class);

	public static <BuiltInReports extends Enum<BuiltInReports>, DefaultReports extends Enum<DefaultReports>>
	void initialize(final Connection conn, final String reportFolder, final Class<BuiltInReports> builtInReports, final Class<DefaultReports> defaultReports, boolean reCompileReports) throws SQLException, InternalErrorException {
		if (Database.isTableEmpty(conn, "report")) {
			if (log.isDebugEnabled())
				log.debug("initializing report table");
			final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
			try {
				final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO report (name,internal_name,template,role_id) VALUES(?,?,?,(SELECT id FROM role WHERE uname LIKE ?));");
				try {
					stmt.clearBatch();
					// load internal reports
					for (BuiltInReports report : builtInReports.getEnumConstants()) {
						stmt.setString(2, report.toString());
						uploadReport(new ReportFile(reportFolder, report.toString().toLowerCase() + "." + IReport.EXTENSION_SOURCE_REPORT), stmt);
					}
					// load standard reports
					stmt.setNull(2, Types.VARCHAR);
					for (DefaultReports report : defaultReports.getEnumConstants())
						uploadReport(new ReportFile(reportFolder, report.toString().toLowerCase() + "." + IReport.EXTENSION_SOURCE_REPORT), stmt);
					if (!Database.isBatchCompleted(stmt.executeBatch()))
						throw new InternalErrorException("failed to initialize report database");
					guard.setSuccess();
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			} finally {
				guard.close();
			}
		}
		if (reCompileReports) {
			final Statement stmt = conn.createStatement();
			try {
				final ResultSet rs = stmt.executeQuery("SELECT template FROM report;");
				while (rs.next()) {
					final ReportFile report = new ReportFile(reportFolder, rs.getString(1));
					if (report.isSource())
						report.compile();
				}
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		}
	}

	private static void uploadReport(final ReportFile report, final PreparedStatement stmtReport) throws InternalErrorException, SQLException {
		Assert.argumentNotNull(report);
		Assert.argumentNotNull(stmtReport);

		final ReportFile.IHeader header = report.getHeader();
		stmtReport.setString(1, header.getTitle());
		stmtReport.setString(3, report.getFileName());
		stmtReport.setString(4, header.getPermission());
		stmtReport.addBatch();
	}

}
