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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.core.Persister;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.dc.server.report.XmlReport;
import com.nabla.wapp.server.auth.AuthDatabase;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
@Singleton
public class MyReadWriteDatabase extends AuthDatabase {

	private static final Log		log = LogFactory.getLog(MyReadWriteDatabase.class);

	private static final String	INTERNAL_REPORT_FOLDER = "/WEB-INF/reports/";
	private static final String[]	RESOURCE_EXTENSIONS = {
		"css","rptlibrary","js"
	};
	private static final String[] REPORT_DESCRIPTOR_EXTENSION = {
		"xml"
	};

	@Inject
	public MyReadWriteDatabase(final ServletContext serverContext) throws SQLException, InternalErrorException, FileNotFoundException {
		super("dcrw", serverContext, IRoles.class, serverContext.getInitParameter("root_password"));

		final Connection conn = getConnection();
		try {
			initializeInternalReports(conn, serverContext);
		} finally {
			conn.close();
		}
	}

	private void initializeInternalReports(final Connection conn, final ServletContext serverContext) throws InternalErrorException, FileNotFoundException, SQLException {
		if (log.isDebugEnabled())
			log.debug("initializing internal report tables");
		final File reportFolder = new File(serverContext.getRealPath(INTERNAL_REPORT_FOLDER));
		loadReportResources(conn, reportFolder);
		loadInternalReports(conn, reportFolder);
	}

	private void loadReportResources(final Connection conn, final File reportFolder) throws SQLException, InternalErrorException, FileNotFoundException {
		Database.executeUpdate(conn, "DELETE FROM report_resource WHERE report_id IS NULL;");
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO report_resource (name,content,report_id) VALUES(?,?,NULL);");
		try {
			stmt.clearBatch();
			@SuppressWarnings("unchecked")
			Iterator<File> it = FileUtils.iterateFiles(reportFolder, RESOURCE_EXTENSIONS, false);
	        while (it.hasNext()){
	        	final File file = it.next();
	        	if (log.isDebugEnabled())
	        		log.debug("uploading resource " + file.getName());
	            final FileInputStream in = new FileInputStream(file);
	            stmt.setString(1, file.getName());
	            stmt.setBlob(2, in);
	            stmt.addBatch();
	        }
	        if (!Database.isBatchCompleted(stmt.executeBatch()))
				throw new InternalErrorException("failed to initialize report_template table");
		} finally {
			stmt.close();
		}
	}

	private void loadInternalReports(final Connection conn, final File reportFolder) throws SQLException, InternalErrorException {
		final Map<String, Integer> locales = getSupportedLocales(conn);
		final Map<String, Integer> roles = getRoles(conn);
		Database.executeUpdate(conn, "DELETE FROM report WHERE internal_name IS NOT NULL;");
		final PreparedStatement stmtReport = conn.prepareStatement(
"INSERT INTO report (internal_name,ledger,role_id,content) VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
		final PreparedStatement stmtName = conn.prepareStatement(
"INSERT INTO report_name_localized (report_id,locale_id,text) VALUES(?,?,?);");
		final Persister serializer = new Persister();
		@SuppressWarnings("unchecked")
		Iterator<File> it = FileUtils.iterateFiles(reportFolder, REPORT_DESCRIPTOR_EXTENSION, false);
        while (it.hasNext()){
        	XmlReport report;
			try {
				report = serializer.read(XmlReport.class, it.next());
			} catch (Exception e) {
				if (log.isErrorEnabled())
					log.error("fail to load report descriptor", e);
				continue;
			}
			if (report.getLocalizedNames().isEmpty()) {
				if (log.isErrorEnabled())
					log.error("no name defined for report '" + report.getFileName() + "'");
				continue;
			}
			if (!report.isDefaultName()) {
				if (log.isErrorEnabled())
					log.error("no default name defined for report '" + report.getFileName() + "'");
				continue;
			}
			final Integer roleId = roles.get(report.getRole());
			if (roleId == null) {
	        	if (log.isErrorEnabled())
	        		log.error("invalid role '" + report.getRole() + "' defined for internal report '" + report.getFileName() + "'");
				continue;
			}
        	if (log.isDebugEnabled())
        		log.debug("uploading internal report '" + report.getFileName() + "'");
        	stmtReport.setString(1, report.getInternalName().toString());
        	stmtReport.setString(2, report.getLedger().toString());
        	stmtReport.setInt(3, roleId);
        	try {
				stmtReport.setBinaryStream(4, new FileInputStream(new File(reportFolder, report.getFileName())));
			} catch (FileNotFoundException e) {
				if (log.isErrorEnabled())
					log.error("fail to find report design '" + report.getFileName() + "'", e);
				continue;
			}
        	if (stmtReport.executeUpdate() != 1) {
				if (log.isErrorEnabled())
					log.error("failed to add internal report '" + report.getFileName() + "'");
			} else {
				final ResultSet rsKey = stmtReport.getGeneratedKeys();
				try {
					rsKey.next();
					stmtName.setInt(1, rsKey.getInt(1));
				} finally {
					rsKey.close();
				}
				// adding default report name
				stmtName.setNull(2, Types.INTEGER);
				stmtName.setString(3, report.getDefaultName());
				stmtName.addBatch();
				for (Map.Entry<String, String> text : report.getLocalizedNames().entrySet()) {
					final Integer localeId = locales.get(text.getKey());
					if (localeId == null) {
						if (log.isErrorEnabled())
							log.error("unsupported locale '" + text.getKey() + "'");
					} else {
						stmtName.setInt(2, localeId);
						stmtName.setString(3, text.getValue());
						stmtName.addBatch();
					}
				}
			}
        }
        if (!Database.isBatchCompleted(stmtName.executeBatch()))
			throw new InternalErrorException("failed to initialize internal report name table");
	}

	private Map<String, Integer> getSupportedLocales(final Connection conn) throws SQLException {
		final Map<String, Integer> locales = new HashMap<String, Integer>();
		final PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM locale;");
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				while (rs.next())
					locales.put(rs.getString(2), rs.getInt(1));
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		return locales;
	}

	private Map<String, Integer> getRoles(final Connection conn) throws SQLException {
		final Map<String, Integer> locales = new HashMap<String, Integer>();
		final PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM role WHERE uname IS NOT NULL;");
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				while (rs.next())
					locales.put(rs.getString(2), rs.getInt(1));
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		return locales;
	}

}
