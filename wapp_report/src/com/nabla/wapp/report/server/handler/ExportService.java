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
package com.nabla.wapp.report.server.handler;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.report.server.ExportReportToCsv;
import com.nabla.wapp.report.server.ExportReportToPdf;
import com.nabla.wapp.report.server.ExportReportToRtf;
import com.nabla.wapp.report.server.ExportReportToXls;
import com.nabla.wapp.report.server.ExportReportToXml;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.print.ReportFormats;


/**
 * @author nabla
 *
 */
@Singleton
public class ExportService extends HttpServlet {

	private static final long	serialVersionUID = 1L;

	private static final Log	log = LogFactory.getLog(ExportService.class);

	private final IDatabase		db;

	@Inject
	public ExportService(@IReadWriteDatabase IDatabase db) {
		this.db = db;
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final String reportId = request.getParameter("id");
		if (reportId == null || reportId.isEmpty()) {
			if (log.isTraceEnabled())
				log.trace("missing report ID");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} else {
			try {
				if (exportReport(reportId, response)) {
				//	response.setStatus(HttpServletResponse.SC_OK);
				} else
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (final SQLException e) {
				if (log.isErrorEnabled())
					log.error("SQL error " + e.getErrorCode() + "-"	+ e.getSQLState(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (final Throwable e) {
				if (log.isErrorEnabled())
					log.error("unexpected error", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	private boolean exportReport(final String reportId, final HttpServletResponse response) throws IOException, SQLException, InternalErrorException {
		final Connection conn = db.getConnection();
		try {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT * FROM export WHERE id=?;", reportId);
			try {
				final ResultSet rsReport = stmt.executeQuery();
				if (!rsReport.next()) {
					if (log.isDebugEnabled())
						log.debug("failed to find report ID= " + reportId);
					return false;
				}
				if (log.isTraceEnabled())
					log.trace("exporting report '" + rsReport.getString("name") + "'");
				final ReportFormats format = ReportFormats.valueOf(rsReport.getString("format"));
				final File reportFile = new File(rsReport.getString("report"));
				try {
					response.setContentType(format.getMimeType());
					if (rsReport.getBoolean("output_as_file")) {
						// IMPORTANT:
						// MUST be done before calling getOutputStream() otherwise no SaveAs dialogbox
						response.setHeader("Content-Disposition", MessageFormat.format(
"attachment; filename=\"{0}.{1}\"", rsReport.getString("name"), format.toString().toLowerCase()));
					}
					final ServletOutputStream out = response.getOutputStream();
					final JRExporter exporter = getReportExporter(format);
					exporter.setParameter(JRExporterParameter.INPUT_FILE, reportFile);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
					try {
						exporter.exportReport();
					} catch (final JRException e) {
						if (log.isDebugEnabled())
							log.debug("failed to generate report", e);
						Util.throwInternalErrorException(e);
					}
					out.flush();
					out.close();
				} finally {
					reportFile.delete();
				}
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
				try {
					Database.executeUpdate(conn, "DELETE FROM export WHERE id=?;", reportId);
				} catch (final SQLException e) {
					if (log.isErrorEnabled())
						log.error("failed to delete export record: " + e.getErrorCode() + "-"	+ e.getSQLState(), e);
				}
			}
		} finally {
			try { conn.close(); } catch (final SQLException e) {}
		}
		return true;
	}

	private JRExporter getReportExporter(final ReportFormats format) throws InternalErrorException {
		switch (format) {
		case CSV:
		case TXT:
			return new ExportReportToCsv();
		case XML:
			return new ExportReportToXml();
	/*	case HTML:
			return new ExportReportToHtml();*/
		case RTF:
			return new ExportReportToRtf();
		case XLS:
			return new ExportReportToXls();
		case PDF:
			return new ExportReportToPdf();
		default:
			if (log.isDebugEnabled())
				log.debug("unsupported format " + format.toString());
			throw new InternalErrorException("unsupported format " + format.toString());
		}
	}

}
