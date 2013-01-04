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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.core.Persister;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.report.server.xml.ReportDesign;
import com.nabla.wapp.server.auth.AuthDatabase;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
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
	private static final String[]	RESOURCE_FILE_EXTENSIONS = {
		".css",".rptlibrary",".js",".properties"
	};
	private static final String	REPORT_FILE_EXTENSION = ".rptdesign";
	private static final String	PROPERTIES_FILE_EXTENSION = ".properties";
	private static final String	PROPERTY_ROLE = "role";
	private static final String	PROPERTY_LEDGER = "ledger";
	private static final String	DEFAULT_LEDGER = Ledgers.SETTINGS.toString();

	@Inject
	public MyReadWriteDatabase(final ServletContext serverContext) throws SQLException, InternalErrorException {
		super("dcrw", serverContext, IRoles.class, serverContext.getInitParameter("root_password"));

		final Connection conn = getConnection();
		try {
			initializeInternalReports(conn, serverContext);
		} finally {
			conn.close();
		}
	}

	private void initializeInternalReports(final Connection conn, final ServletContext serverContext) throws InternalErrorException, SQLException {
		if (log.isDebugEnabled())
			log.debug("loading internal report table");
		final Map<String, Integer> roles = getRoles(conn);
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			Database.executeUpdate(conn, "DELETE FROM report WHERE internal_name IS NOT NULL;");
			final File reportFolder = new File(serverContext.getRealPath(INTERNAL_REPORT_FOLDER));
			for (File folder : reportFolder.listFiles((FileFilter)DirectoryFileFilter.INSTANCE))
				loadReport(conn, folder, roles);
			guard.setSuccess();
		} finally {
			guard.close();
		}
	}

	private void loadReport(final Connection conn, final File folder, final Map<String, Integer> roles) throws SQLException, InternalErrorException {
	// load and scan report design
		final File[] files = folder.listFiles((FileFilter)new SuffixFileFilter(REPORT_FILE_EXTENSION));
		if (files.length != 1) {
        	if (log.isDebugEnabled())
        		log.debug("skip folder '" + folder.getName() + "': no report file found");
        	return;
        }
        final File reportFile = files[0];
        ReportDesign report;
		try {
			report = new Persister().read(ReportDesign.class, reportFile);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("fail to load report design", e);
			return;
		}
	// add report record
		final Integer roleId = roles.get(report.getProperty(PROPERTY_ROLE, null));
		if (roleId == null) {
        	if (log.isErrorEnabled())
        		log.error("invalid role '" + report.getProperty(PROPERTY_ROLE) + "' defined for report '" + reportFile.getName() + "'");
        	return;
		}
		Ledgers ledger;
		try {
			ledger = Ledgers.valueOf(report.getProperty(PROPERTY_LEDGER, DEFAULT_LEDGER));
		} catch (Exception __) {
        	if (log.isErrorEnabled())
        		log.error("invalid ledger '" + report.getProperty(PROPERTY_LEDGER) + "' defined for report '" + reportFile.getName() + "'");
        	return;
		}
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO report (name,internal_name,ledger,role_id,content) VALUES(?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
		try {
			stmt.setString(1, report.getDefaultTitle(reportFile.getName()));
	    	stmt.setString(2, folder.getName());
	    	stmt.setString(3, ledger.toString());
	    	stmt.setInt(4, roleId);
	    	try {
				stmt.setBinaryStream(5, new FileInputStream(reportFile));
			} catch (FileNotFoundException e) {
				if (log.isErrorEnabled())
					log.error("fail to find report design '" + reportFile.getName() + "'", e);
				return;
			}
	    	if (log.isDebugEnabled())
        		log.debug("uploading report " + reportFile.getName());
	    	if (stmt.executeUpdate() != 1) {
				if (log.isErrorEnabled())
					log.error("failed to add internal report '" + reportFile.getName() + "'");
				return;
			}
			final ResultSet rsKey = stmt.getGeneratedKeys();
			try {
				rsKey.next();
				final Integer reportId = rsKey.getInt(1);
				loadReportResources(conn, reportId, folder);
				loadLocaleReportNames(conn, reportId, report.getTitleKey(), folder);
			} finally {
				rsKey.close();
			}
		} finally {
			stmt.close();
		}
	}

	private void loadLocaleReportNames(final Connection conn, final Integer reportId, final String titleKey, final File reportFolder) throws SQLException, InternalErrorException {
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO report_name_localized (report_id,locale,text) VALUES(?,?,?);");
		try {
			stmt.clearBatch();
			stmt.setInt(1, reportId);
			for (File file : reportFolder.listFiles((FileFilter)new SuffixFileFilter(PROPERTIES_FILE_EXTENSION))) {
	        // extract locale from file name: xxxx_en_GB.properties
	        	final String fileName = file.getName();
	        	int pos = fileName.indexOf('_');
				if (pos < 0)
					continue;
				final String locale = fileName.substring(pos + 1, fileName.length() - PROPERTIES_FILE_EXTENSION.length());
				try {
					stmt.setString(2, LocaleUtils.toLocale(locale).toString());
				} catch (IllegalArgumentException __) {
					if (log.isErrorEnabled())
						log.error("unsupported locale '" + locale + "'");
					continue;
				}
			// get title in locale
	        	final Properties properties = new Properties();
	        	try {
					properties.load(new FileInputStream(file));
				} catch (Exception e) {
					if (log.isErrorEnabled())
	    				log.error("fail to load locale properties file '" + file.getName() + "'", e);
	    			continue;
				}
	        	final String title = properties.getProperty(titleKey);
	        	if (title == null)
	        		continue;
	        	stmt.setString(3, title);
	        	stmt.addBatch();
	        }
	        if (!Database.isBatchCompleted(stmt.executeBatch()))
				throw new InternalErrorException("failed to add locale report titles");
		} finally {
			stmt.close();
		}
	}

	private void loadReportResources(final Connection conn, final Integer reportId, final File reportFolder) throws SQLException, InternalErrorException {
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO report_resource (report_id,name,content) VALUES(?,?,?);");
		try {
			stmt.clearBatch();
			stmt.setInt(1, reportId);
			for (File file : reportFolder.listFiles((FileFilter)new SuffixFileFilter(RESOURCE_FILE_EXTENSIONS))) {
	        	if (log.isDebugEnabled())
	        		log.debug("uploading resource " + file.getName());
	            stmt.setString(2, file.getName());
	            try {
					stmt.setBlob(3, new FileInputStream(file));
				} catch (FileNotFoundException e) {
					if (log.isErrorEnabled())
	    				log.error("fail to load resource file '" + file.getName() + "'", e);
	    			continue;
				}
	            stmt.addBatch();
	        }
	        if (!Database.isBatchCompleted(stmt.executeBatch()))
				throw new InternalErrorException("failed to load report resources");
		} finally {
			stmt.close();
		}
	}

	private Map<String, Integer> getRoles(final Connection conn) throws SQLException {
		final Map<String, Integer> locales = new HashMap<String, Integer>();
		final PreparedStatement stmt = conn.prepareStatement(
"SELECT id, name FROM role WHERE uname IS NOT NULL;");
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
