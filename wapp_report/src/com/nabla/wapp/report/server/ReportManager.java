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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import com.google.inject.Singleton;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.auth.AccessDeniedException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla64
 *
 */
@Singleton
public class ReportManager {

	private static final Log		log = LogFactory.getLog(ReportManager.class);

	private final IReportEngine	engine;

	public ReportManager() throws BirtException {
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

	public ReportTemplate open(final String internalName, final IUserSessionContext ctx, final String locale) throws SQLException, DispatchException {
		final PreparedStatement stmt = ctx.isRoot() ?
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content" +
" FROM report AS r" +
" WHERE r.internal_name=?;", internalName)
				:
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content" +
" FROM report AS r INNER JOIN user_role ON r.role_id=user_role.role_id" +
" WHERE r.internal_name=? AND user_role.user_id=?;", internalName, ctx.getUserId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				return openDesign(ctx.getReadConnection(), rs, locale);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	public ReportTemplate open(final Integer id, final IUserSessionContext ctx, final String locale) throws SQLException, DispatchException {
		final PreparedStatement stmt = ctx.isRoot() ?
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content" +
" FROM report AS r" +
" WHERE r.id=?;", id)
				:
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content" +
" FROM report AS r INNER JOIN user_role ON r.role_id=user_role.role_id" +
" WHERE r.id=? AND user_role.user_id=?;", id, ctx.getUserId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				return openDesign(ctx.getReadConnection(), rs, locale);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	protected ReportTemplate openDesign(final Connection conn, final ResultSet rs, final String locale) throws DispatchException, SQLException {
		if (!rs.next())
			throw new AccessDeniedException();
		final Integer id = rs.getInt(1);
		try {
			return new ReportTemplate(engine.openReportDesign(getLocalizedReportName(conn, id, locale), rs.getBinaryStream(2), new StreamResolvingResourceLocator(conn, id)));
		} catch (EngineException e) {
			Util.throwInternalErrorException(e);
			return null;
		}
	}

	protected String getLocalizedReportName(final Connection conn, final Integer reportId, final String locale) throws SQLException, InternalErrorException {
		final PreparedStatement stmt = 	StatementFormat.prepare(conn,
"SELECT COALESCE((" +
"SELECT n.text" +
" FROM report_name_localized AS n INNER JOIN locale AS l ON n.locale_id=l.id" +
" WHERE n.report_id=? AND l.name LIKE ?" +
"),(" +
"SELECT n.text" +
" FROM report_name_localized AS n" +
" WHERE n.report_id=? AND n.locale_id IS NULL" +
"));", reportId, locale, reportId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new InternalErrorException("no name defined for report " + reportId);
				if (log.isDebugEnabled())
					log.debug("report name for locale '" + locale + "': '" + rs.getString(1) + "'");
				return rs.getString(1);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

}
