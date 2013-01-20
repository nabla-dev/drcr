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
package com.nabla.wapp.report.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.ServletContext;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.simpleframework.xml.core.Persister;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.report.server.xml.ReportDesign;
import com.nabla.wapp.report.shared.ReportErrors;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.auth.AccessDeniedException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.general.Nullable;

@Singleton
public class ReportManager {

	private static final Log		log = LogFactory.getLog(ReportManager.class);

	public static final String	INTERNAL_REPORT_FOLDER = "/WEB-INF/reports/";
	public static final String	REPORT_FILE_EXTENSION = "rptdesign";
	public static final String	PROPERTIES_FILE_EXTENSION = "properties";
	public static final String[]	RESOURCE_FILE_EXTENSIONS = {
		"css","rptlibrary","js","properties"
	};

	private final String						internalReportFolder;
	private final IReportEngine				engine;
	private final IReportCategoryValidator		reportCategoryValidator;

	@Inject
	public ReportManager(final ServletContext serverContext, final IReportCategoryValidator reportCategoryValidator) throws BirtException {
		this.reportCategoryValidator = reportCategoryValidator;
		internalReportFolder = "1".equals(serverContext.getInitParameter(Database.PRODUCTION_MODE)) ? null : serverContext.getRealPath(INTERNAL_REPORT_FOLDER);
		if (log.isDebugEnabled())
			log.debug("initializing BIRT report engine");
		final EngineConfig config = new EngineConfig( );
		Platform.startup( config );
		final IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		engine = factory.createReportEngine( config );
		engine.changeLogLevel( Level.WARNING );
		/*
		  	engine.destroy();
			Platform.shutdown();
		*/
	}

	public ReportTemplate open(final String internalName, final IUserSessionContext ctx) throws SQLException, DispatchException {
		if (internalReportFolder != null) {
			final File reportFile = new File(internalReportFolder + internalName.toUpperCase(), internalName.toLowerCase() + ".rptdesign");
			try {
				return new ReportTemplate(internalName, engine.openReportDesign(reportFile.getAbsolutePath()));
			} catch (EngineException e) {
				throw new InternalErrorException(Util.formatInternalErrorDescription(e));
			}
		} else {
			final PreparedStatement stmt = ctx.isRoot() ?
					StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content, COALESCE(n.text, r.name) AS 'name'" +
" FROM report AS r LEFT JOIN report_name_localized AS n ON r.id=n.report_id AND n.locale LIKE ?" +
" WHERE r.internal_name LIKE ?;", ctx.getLocale().toString(), internalName)
					:
					StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content, COALESCE(n.text, r.name) AS 'name'" +
" FROM user_role AS p INNER JOIN (" +
"report AS r LEFT JOIN report_name_localized AS n ON r.id=n.report_id AND n.locale LIKE ?" +
") ON r.role_id=p.role_id" +
" WHERE r.internal_name LIKE ? AND p.user_id=?;", ctx.getLocale().toString(), internalName, ctx.getUserId());
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next())
						throw new AccessDeniedException();
					return open(rs, ctx);
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		}
	}

	public ReportTemplateList open(final IntegerSet reportIds, final IUserSessionContext ctx) throws SQLException, DispatchException {
		final ReportTemplateList templates = new ReportTemplateList();
		final PreparedStatement stmt = ctx.isRoot() ?
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content, COALESCE(n.text, r.name) AS 'name'" +
" FROM report AS r LEFT JOIN report_name_localized AS n ON r.id=n.report_id AND n.locale LIKE ?" +
" WHERE r.id IN (?);", ctx.getLocale().toString(), reportIds)
				:
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content, COALESCE(n.text, r.name) AS 'name'" +
" FROM user_role AS p INNER JOIN (" +
"report AS r LEFT JOIN report_name_localized AS n ON r.id=n.report_id AND n.locale LIKE ?" +
") ON r.role_id=p.role_id" +
" WHERE p.user_id=? AND r.id IN (?);", ctx.getLocale().toString(), ctx.getUserId(), reportIds);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				while (rs.next())
					templates.add(open(rs, ctx));
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		if (templates.size() != reportIds.size())
			throw new AccessDeniedException();
		return templates;
	}

	public ReportTemplate open(final ResultSet rs, final IUserSessionContext ctx) throws SQLException, DispatchException {
		try {
			final Integer id = rs.getInt("id");
			return new ReportTemplate(id.toString(), engine.openReportDesign(rs.getString("name"), rs.getBinaryStream("content"), new StreamResolvingResourceLocator(ctx.getReadConnection(), id)));
		} catch (EngineException e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
	}

	public int addReport(final Connection conn, final String reportName, final InputStream design, final InputStream in) throws SQLException, DispatchException {
		return addReport(conn, reportName, null, design, in);
	}

	public int addReport(final Connection conn, final String reportName, @Nullable final String internalName, final InputStream design, final InputStream in) throws SQLException, DispatchException {
	// load and scan report design
		if (log.isDebugEnabled())
    		log.debug("scanning report " + reportName);
		ReportDesign report;
		try {
			report = new Persister().read(ReportDesign.class, design);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("fail to load report design", e);
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
	// add report record
		final Integer roleId = getRole(conn, report.getRole());
		if (roleId == null) {
			if (log.isErrorEnabled())
				log.error("invalid role '" + report.getRole() + "' defined for report '" + reportName + "'");
        	throw new DispatchException(ReportErrors.REPORT_DESIGN_INVALID_ROLE);
		}
		final String category = report.getCategory();
		if (!reportCategoryValidator.isValid(category)) {
			if (log.isErrorEnabled())
				log.error("invalid category '" + category + "' defined for report ' " + reportName + "'");
       		throw new DispatchException(ReportErrors.REPORT_DESIGN_INVALID_CATEGORY);
		}
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO report (name,internal_name,category,role_id,content) VALUES(?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
		try {
			stmt.setString(1, report.getTitle());
			if (internalName != null)
				stmt.setString(2, internalName);
			else
	    		stmt.setNull(2, Types.VARCHAR);
	    	if (category != null)
	    		stmt.setString(3, category);
	    	else
	    		stmt.setNull(3, Types.VARCHAR);
	    	stmt.setInt(4, roleId);
			stmt.setBinaryStream(5, in);
	    	if (log.isDebugEnabled())
        		log.debug("uploading report " + reportName);
	    	if (stmt.executeUpdate() != 1) {
				if (log.isErrorEnabled())
					log.error("failed to add internal report '" + reportName + "'");
				throw new InternalErrorException();
			}
			final ResultSet rsKey = stmt.getGeneratedKeys();
			try {
				rsKey.next();
				return rsKey.getInt(1);
			} finally {
				rsKey.close();
			}
		} finally {
			stmt.close();
		}
	}

	public void upgradeReport(final Connection conn, final Integer reportId, final String reportName, final InputStream in) throws SQLException, DispatchException {
	// load and scan report design
		ReportDesign report;
		try {
			report = new Persister().read(ReportDesign.class, in);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("fail to load report design", e);
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
	// update report record
    	if (log.isDebugEnabled())
    		log.debug("uploading report " + reportName);
    	if (!Database.executeUpdate(conn,
"UPDATE report SET name=?,content=? WHERE id=?;", report.getTitle(), in, reportId)) {
			if (log.isErrorEnabled())
				log.error("failed to upgrade internal report '" + reportName + "'");
			throw new InternalErrorException();
		}
	}

	public void loadLocaleReportName(final Connection conn, final Integer reportId, final String fileName, final InputStream resource) throws SQLException, InternalErrorException {
		final String name = FilenameUtils.getBaseName(fileName);
		int pos = name.indexOf('_');
		if (pos < 0)
			return;
		final String locale = name.substring(pos + 1, name.length());
		try {
			LocaleUtils.toLocale(locale);
		} catch (IllegalArgumentException __) {
			if (log.isErrorEnabled())
				log.error("unsupported locale '" + locale + "'");
			return;
		}
		// get title in locale
    	final Properties properties = new Properties();
    	try {
			properties.load(resource);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("fail to load locale properties file '" + fileName + "'", e);
			return;
		}
    	final String title = properties.getProperty(ReportDesign.TITLE);
    	if (title == null)
    		return;
		if (!Database.executeUpdate(conn,
"INSERT INTO report_name_localized (report_id,locale,text) VALUES(?,?,?);",
				reportId, locale, title))
			throw new InternalErrorException("failed to add locale report title");
	}

	public void loadReportResource(final Connection conn, final Integer reportId, final String fileName, final InputStream resource) throws SQLException, InternalErrorException {
		if (log.isDebugEnabled())
    		log.debug("uploading resource " + fileName);
		if (!Database.executeUpdate(conn,
"INSERT INTO report_resource (report_id,name,content) VALUES(?,?,?);",
				reportId, fileName, resource))
			throw new InternalErrorException("failed to load report resource '" + fileName + "'");
	}

	public static File copyStreamToTempFile(final InputStream in) throws InternalErrorException {
		File rslt;
		try {
			rslt = File.createTempFile("wapp_report", null);
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error("fail to create temporary file to hold stream");
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
		try {
			final FileOutputStream out = new FileOutputStream(rslt);
			IOUtils.copy(in, out);
			out.close();
		} catch (Throwable e) {
			rslt.delete();
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		}
		return rslt;
	}

	protected static Integer getRole(final Connection conn, final String name) throws SQLException {
		if (name != null) {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT id FROM role WHERE name LIKE ? AND uname IS NOT NULL;", name);
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (rs.next())
						return rs.getInt(1);
				} finally {
					rs.close();
				}
			} finally {
				stmt.close();
			}
		}
		return null;
	}

}
