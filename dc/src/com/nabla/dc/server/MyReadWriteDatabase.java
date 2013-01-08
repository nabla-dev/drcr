/**
* Copyright 2012 nabla
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
package com.nabla.dc.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.server.auth.AuthDatabase;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;


@Singleton
public class MyReadWriteDatabase extends AuthDatabase {

	private static final Log		log = LogFactory.getLog(MyReadWriteDatabase.class);

	@Inject
	public MyReadWriteDatabase(final ServletContext serverContext, final ReportManager reportManager) throws SQLException, DispatchException {
		super("dcrw", serverContext, IRoles.class, serverContext.getInitParameter("root_password"));

		if ("1".equals(serverContext.getInitParameter(PRODUCTION_MODE))) {
			if (log.isDebugEnabled())
				log.debug("loading internal report table");
			final Connection conn = getConnection();
			try {
				final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
				try {
					Database.executeUpdate(conn, "DELETE FROM report WHERE internal_name IS NOT NULL;");
					final File reportFolder = new File(serverContext.getRealPath(ReportManager.INTERNAL_REPORT_FOLDER));
					for (File folder : reportFolder.listFiles((FileFilter)DirectoryFileFilter.INSTANCE))
						loadInternalReport(conn, folder, reportManager);
					guard.setSuccess();
				} finally {
					guard.close();
				}
			} finally {
				conn.close();
			}
		}
	}

	private boolean loadInternalReport(final Connection conn, final File folder, final ReportManager reportManager) throws SQLException, DispatchException {
		final File[] files = folder.listFiles((FileFilter)new SuffixFileFilter(ReportManager.REPORT_FILE_EXTENSION));
		if (files.length != 1) {
        	if (log.isDebugEnabled())
        		log.debug("skip folder '" + folder.getName() + "': no report file found");
        	return false;
        }
        final File reportFile = files[0];
        InputStream in;
		try {
			in = new FileInputStream(reportFile);
		} catch (FileNotFoundException e) {
			Util.throwInternalErrorException(e);
			return false;
		}
		int reportId = reportManager.addReport(conn, FilenameUtils.getBaseName(reportFile.getName()), folder.getName(), in);
		for (File file : folder.listFiles((FileFilter)new SuffixFileFilter(ReportManager.RESOURCE_FILE_EXTENSIONS))) {
        	try {
				reportManager.loadReportResource(conn, reportId, file.getName(), new FileInputStream(file));
			} catch (FileNotFoundException e) {
				Util.throwInternalErrorException(e);
			}
		}
		for (File file : folder.listFiles((FileFilter)new SuffixFileFilter(ReportManager.PROPERTIES_FILE_EXTENSION))) {
			try {
				reportManager.loadLocaleReportName(conn, reportId, file.getName(), new FileInputStream(file));
			} catch (FileNotFoundException e) {
				Util.throwInternalErrorException(e);
			}
		}
		return true;
	}
}
