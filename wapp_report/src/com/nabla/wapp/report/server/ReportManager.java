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

	public ReportTemplate open(final String internalName, final IUserSessionContext ctx) throws SQLException, DispatchException {
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
				return openDesign(rs, ctx);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	public ReportTemplate open(final Integer id, final IUserSessionContext ctx) throws SQLException, DispatchException {
		final PreparedStatement stmt = ctx.isRoot() ?
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content, COALESCE(n.text, r.name) AS 'name'" +
" FROM report AS r LEFT JOIN report_name_localized AS n ON r.id=n.report_id AND n.locale LIKE ?" +
" WHERE r.id=?;", ctx.getLocale().toString(), id)
				:
				StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content, COALESCE(n.text, r.name) AS 'name'" +
" FROM user_role AS p INNER JOIN (" +
"report AS r LEFT JOIN report_name_localized AS n ON r.id=n.report_id AND n.locale LIKE ?" +
") ON r.role_id=p.role_id" +
" WHERE r.id=? AND p.user_id=?;", ctx.getLocale().toString(), id, ctx.getUserId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				return openDesign(rs, ctx);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	protected ReportTemplate openDesign(final ResultSet rs, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (!rs.next())
			throw new AccessDeniedException();
		final Integer id = rs.getInt(1);
		try {
			return new ReportTemplate(engine.openReportDesign(rs.getString(3), rs.getBinaryStream(2), new StreamResolvingResourceLocator(ctx.getReadConnection(), id)));
		} catch (EngineException e) {
			Util.throwInternalErrorException(e);
			return null;
		}
	}

}
