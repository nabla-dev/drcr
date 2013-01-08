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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FilenameUtils;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.report.server.ReportZipFile;
import com.nabla.wapp.report.shared.ReportErrors;
import com.nabla.wapp.report.shared.command.UpgradeReport;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

public class UpgradeReportHandler extends AbstractUpdateHandler<UpgradeReport> {

	private final ReportManager	reportManager;

	@Inject
	public UpgradeReportHandler(final ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	@Override
	protected void update(UpgradeReport record, IUserSessionContext ctx) throws DispatchException, SQLException {
		final Connection conn = ctx.getWriteConnection();
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT * FROM import_data WHERE id=?;", record.getFileId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new ActionException(CommonServerErrors.NO_DATA);
				if (!ctx.getSessionId().equals(rs.getString("userSessionId")))
					throw new ActionException(CommonServerErrors.ACCESS_DENIED);
				final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
				try {
					Database.executeUpdate(conn,
"DELETE report_resource WHERE report_id=?;", record.getReportId());
					Database.executeUpdate(conn,
"DELETE report_name_localized WHERE report_id=?;", record.getReportId());
					if (ReportZipFile.acceptContentType(rs.getString("content_type"))) {
						final ReportZipFile zip = new ReportZipFile(rs.getBinaryStream("content"));
						try {
							final ZipArchiveEntry design = zip.getReportDesign();
							if (design == null)
								throw new DispatchException(ReportErrors.ADD_REPORT_NO_REPORT_DESIGN_FOUND);
							reportManager.upgradeReport(conn, record.getReportId(), design.getName(), zip.getInputStream(design));
							for (Enumeration<ZipArchiveEntry> iter = zip.getEntries(ReportManager.PROPERTIES_FILE_EXTENSION); iter.hasMoreElements();) {
								final ZipArchiveEntry e = iter.nextElement();
								reportManager.loadLocaleReportName(conn, record.getReportId(), e.getName(), zip.getInputStream(e));
							}
							for (Enumeration<ZipArchiveEntry> iter = zip.getEntries(ReportManager.RESOURCE_FILE_EXTENSIONS); iter.hasMoreElements();) {
								final ZipArchiveEntry e = iter.nextElement();
								reportManager.loadReportResource(conn, record.getReportId(), e.getName(), zip.getInputStream(e));
							}
						} finally {
							zip.close();
						}
					} else
						reportManager.upgradeReport(conn, record.getReportId(), FilenameUtils.getBaseName(rs.getString("file_name")), rs.getBinaryStream("content"));
					guard.setSuccess();
				} finally {
					guard.close();
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		Database.executeUpdate(conn, "DELETE FROM import_data WHERE id=?;", record.getFileId());
	}

}
