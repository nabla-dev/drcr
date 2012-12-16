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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import com.google.inject.Singleton;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.auth.AccessDeniedException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.print.ReportFormats;

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

	public IReportRunnable openReportTemplate(final String internalName, final IUserSessionContext ctx) throws SQLException, DispatchException {
		/*	final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT *" +
" FROM report INNER JOIN user_role ON report.role_id=user_role.role_id" +
" WHERE user_role.user_id=? AND report.internal_name=?;",
ctx.getUserId(), cmd.getName());
		try {
			final ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new AccessDeniedException();*/
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.id, r.content" +
" FROM report AS r" +
" WHERE r.internal_name=?;", internalName);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new AccessDeniedException();
				final Integer id = rs.getInt(1);
				try {
					return engine.openReportDesign(id.toString(), rs.getBinaryStream(2), new StreamResolvingResourceLocator(ctx.getReadConnection(), id));
				} catch (EngineException e) {
					Util.throwInternalErrorException(e);
					return null;
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	public IReportRunnable openReportTemplate(final Integer id, final IUserSessionContext ctx) throws SQLException, DispatchException {
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT r.content" +
" FROM report AS r" +
" WHERE r.id=?;", id);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new AccessDeniedException();
				try {
					return engine.openReportDesign(id.toString(), rs.getBinaryStream(1), new StreamResolvingResourceLocator(ctx.getReadConnection(), id));
				} catch (EngineException e) {
					Util.throwInternalErrorException(e);
					return null;
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	@SuppressWarnings("unchecked")
	public InputStream createReport(final IReportRunnable template, final Connection conn, final Map<String, Object> parameters, final ReportFormats format) throws InternalErrorException {
		final IRunAndRenderTask t = engine.createRunAndRenderTask(template);
		try {
			t.getAppContext().put("OdaJDBCDriverPassInConnection", conn);
			for (Map.Entry<String, Object> parameter : parameters.entrySet())
				t.setParameterValue(parameter.getKey(), parameter.getValue());
			if (!parameters.isEmpty() && !t.validateParameters())
				Util.throwInternalErrorException("invalid parameters for report '" + template.getReportName() + "'");
			final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
			try {
				switch (format) {
				case CSV:

					break;
				case RTF:
					break;
				case TXT:
					break;
				case XLS:
					createExcel(t, tmp);
					break;
				case XML:
					break;
				case PDF:
				default:
					createPDF(t, tmp);
					break;
				}
			} catch (EngineException e) {
				Util.throwInternalErrorException(e);
				return null;
			}
			return new ByteArrayInputStream(tmp.toByteArray());
		} finally {
			t.close();
		}
	}

	public Integer saveReport(final Connection conn, final String name, final InputStream report, final ReportFormats format, final Boolean outputAsFile) throws SQLException, InternalErrorException {
		final Integer id = Database.addRecord(conn,
"INSERT INTO export (name,content_type,output_as_file,content) VALUES(?,?,?,?);",
name, format.getMimeType(), outputAsFile, report);
		if (id == null)
			Util.throwInternalErrorException("fail to save report to be exported '" + name + "'");
		return id;
	}

	public Set<Integer> saveReports(final Connection conn, final Map<String, InputStream> reports, final ReportFormats format, final Boolean outputAsFile) throws SQLException, InternalErrorException {
		final Set<Integer> ids = new HashSet<Integer>();
		if (format == ReportFormats.PDF) {
			// merge PDFs into one

		} else {
			for (Map.Entry<String, InputStream> report : reports.entrySet())
				ids.add(saveReport(conn, report.getKey(), report.getValue(), format, outputAsFile));
		}
		return ids;
	}

	private static void createPDF(final IRunAndRenderTask t, final OutputStream out) throws EngineException {
		final PDFRenderOption options = new PDFRenderOption();
		//	options.setOption(IPDFRenderOption.PAGE_OVERFLOW, new Integer(IPDFRenderOption.FIT_TO_PAGE_SIZE) );
		options.setOutputStream(out);
		options.setOutputFormat("pdf");
		t.setRenderOption(options);
		t.run();
	}

	private static void createExcel(final IRunAndRenderTask t, final OutputStream out) throws EngineException {
		final EXCELRenderOption options = new EXCELRenderOption();
		options.setOutputStream(out);
		t.setRenderOption(options);
		t.run();
	}
}
