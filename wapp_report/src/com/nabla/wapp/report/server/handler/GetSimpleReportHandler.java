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
package com.nabla.wapp.report.server.handler;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nabla.wapp.shared.dispatch.DispatchException;
import net.sf.jasperreports.engine.JRParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.IReportFolder;
import com.nabla.wapp.report.server.IReportParameterTypeValidator;
import com.nabla.wapp.report.server.ReportFile;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.ReportParameter;
import com.nabla.wapp.report.shared.SimpleReportResult;
import com.nabla.wapp.report.shared.command.GetSimpleReport;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.auth.AccessDeniedException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
public class GetSimpleReportHandler extends AbstractHandler<GetSimpleReport, SimpleReportResult> {

	private static final Log					log = LogFactory.getLog(GetSimpleReportHandler.class);
	private final String						reportFolder;
	private final IReportParameterTypeValidator	parameterTypeValidator;

	@Inject
	public GetSimpleReportHandler(@IReportFolder final String reportFolder, final IReportParameterTypeValidator parameterTypeValidator) {
		super(true);
		this.reportFolder = reportFolder;
		this.parameterTypeValidator = parameterTypeValidator;
	}

	@Override
	public SimpleReportResult execute(final GetSimpleReport cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// get list of reports to generate
		final PreparedStatement stmtReport = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT report.id, report.name, report.template FROM report INNER JOIN user_role ON report.role_id=user_role.role_id  WHERE user_role.user_id=? AND report.id IN(?);",
ctx.getUserId(), cmd.getReportIds());
		try {
			final ResultSet rsReport = stmtReport.executeQuery();
			int reportFound = 0;
			final Map<String, Object> parameters = cmd.getParameters();
			final Map<String, ReportParameter> reportParameters = new HashMap<String, ReportParameter>();
			final ReportFile report = new ReportFile(reportFolder);
			while (rsReport.next()) {
				if (log.isDebugEnabled())
					log.debug("report to generate: '" + rsReport.getString("name") + "'");
				++reportFound;
				report.setFileName(rsReport.getString("template"));
				for (JRParameter parameter : report.getHeader().getParameters()) {
					if (!parameter.isForPrompting() || parameter.isSystemDefined())
						continue;
					final String name = parameter.getName();
					if (!parameters.containsKey(name)) {
						final String model = parameter.getPropertiesMap().getProperty(IReport.HEADER_PARAMETER_TYPE);
						if (!parameterTypeValidator.isValid(model)) {
							if (log.isErrorEnabled())
								log.error("model '" + model + "' for parameter '" + name + "' is invalid");
							throw new InternalErrorException("model '" + model + "' for parameter '" + name + "' is invalid");
						}
						reportParameters.put(name, new ReportParameter(name, parameter.getDescription(), model));
					}
				}
			}
			if (reportFound != cmd.getReportIds().size())
				throw new AccessDeniedException();
			if (!reportParameters.isEmpty()) {
				if (log.isDebugEnabled())
					log.debug("need extra parameters: " + reportParameters.size());
				return new SimpleReportResult(reportParameters.values());
			}
			// generate all reports
			final Set<Integer> reportIds = new HashSet<Integer>();
			final List<File> files = new LinkedList<File>();
			try {
				final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
				try {
					final PreparedStatement stmtExport = ctx.getWriteConnection().prepareStatement(
"INSERT INTO export (name,format,output_as_file,report) VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
					try {
						stmtExport.setString(2, cmd.getFormat().toString());
						stmtExport.setBoolean(3, cmd.getOutputAsFile());
						rsReport.beforeFirst();
						while (rsReport.next()) {
							report.setFileName(rsReport.getString("template"));
							final File file = report.generate(parameters, ctx.getReadConnection());
							files.add(file);
							stmtExport.setString(1, rsReport.getString("name"));
							stmtExport.setString(4, file.getAbsolutePath());
							stmtExport.addBatch();
						}
						if (!Database.isBatchCompleted(stmtExport.executeBatch()))
							throw new InternalErrorException("failed to save report to database");
						final ResultSet rsKey = stmtExport.getGeneratedKeys();
						while (rsKey.next())
							reportIds.add(rsKey.getInt(1));
						guard.setSuccess();
					} finally {
						try { stmtExport.close(); } catch (final SQLException e) {}
					}
				} finally {
					guard.close();
				}
				files.clear();	// not to delete report files in final clause
				return new SimpleReportResult(reportIds);
			} finally {
				for (File e : files)
					e.delete();
			}
		} finally {
			try { stmtReport.close(); } catch (final SQLException e) {}
		}
	}

}
