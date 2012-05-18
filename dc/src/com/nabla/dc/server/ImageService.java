/**
 Copyright 2012 nabla
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadOnlyDatabase;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author FNorais
 *
 */
@Singleton
public class ImageService extends HttpServlet {

	private static final long	serialVersionUID = 1L;
	private static final Log	log = LogFactory.getLog(ImageService.class);
	private static final int	DEFAULT_BUFFER_SIZE = 10240;	// 10KB
	private final IDatabase		db;

	@Inject
	public ImageService(@IReadOnlyDatabase IDatabase db) {
		this.db = db;
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final String imageId = request.getParameter("id");
		if (imageId == null || imageId.isEmpty()) {
			if (log.isTraceEnabled())
				log.trace("missing image ID");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			try {
				if (exportImage(imageId, response)) {
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

	private boolean exportImage(final String imageId, final HttpServletResponse response) throws IOException, SQLException, InternalErrorException {
		final Connection conn = db.getConnection();
		try {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT * FROM logo WHERE id=?;", imageId);
			try {
				final ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					if (log.isDebugEnabled())
						log.debug("failed to find report ID= " + imageId);
					return false;
				}
				if (log.isTraceEnabled())
					log.trace("exporting image " + imageId);
				response.reset();
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setContentType(rs.getString("content_type"));
				response.setHeader("Content-Length", String.valueOf(rs.getInt("length")));
				response.setHeader("Content-Disposition", MessageFormat.format(
						"inline; filename=\"{0}\"", rs.getString("name")));
				final BufferedInputStream input = new BufferedInputStream(rs.getBinaryStream("content"), DEFAULT_BUFFER_SIZE);
				try {
					final BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
					try {
						final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
						int length;
						while ((length = input.read(buffer)) > 0)
							output.write(buffer, 0, length);
					} finally {
						output.close();	
					}
				} finally {
					input.close();
				}
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		} finally {
			try { conn.close(); } catch (final SQLException e) {}
		}
		return true;
	}

}
