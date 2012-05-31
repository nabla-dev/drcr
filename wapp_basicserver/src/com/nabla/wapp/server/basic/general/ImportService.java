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
package com.nabla.wapp.server.basic.general;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.general.Assert;


/**
 * @author nabla
 *
 */
@Singleton
public class ImportService extends UploadAction {

	private static final long	serialVersionUID = 1L;
	private static final Log	log = LogFactory.getLog(ImportService.class);
	private final IDatabase		db;

	@Inject
	public ImportService(@IReadWriteDatabase final IDatabase db) {
		this.db = db;
	}

	@Override
	public String executeAction(final HttpServletRequest request, final List<FileItem> sessionFiles) throws UploadActionException {
		Assert.state(sessionFiles.size() == 1);
		try {
			for (FileItem file : sessionFiles) {
				if (file.isFormField())
					continue;
				if (log.isDebugEnabled())
					log.debug("field '" + file.getFieldName() + "': uploading " + file.getName());
				log.debug("field: " + file.getFieldName());
				log.debug("filename: " + file.getName());
				log.debug("content_type: " + file.getContentType());
				log.debug("size: " + file.getSize());
				final Connection conn = db.getConnection();
				try {
					final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO import_data (field_name, file_name, content_type, length, content) VALUES(?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
					try {
						stmt.setString(1, file.getFieldName());
						stmt.setString(2, file.getName());
						stmt.setString(3, file.getContentType());
						stmt.setLong(4, file.getSize());
						final InputStream fs = file.getInputStream();
						try {
		   					stmt.setBinaryStream(5, fs);
		   					if (stmt.executeUpdate() != 1) {
		   						if (log.isErrorEnabled())
		   							log.error("failed to add imported file record");
		   						throw new UploadActionException("internal error");
		   					}
		   					final ResultSet rsKey = stmt.getGeneratedKeys();
		   					rsKey.next();
		   					final Integer id = rsKey.getInt(1);
		   					if (log.isDebugEnabled())
		   						log.debug("uploading " + file.getName() + " successfully completed. id = " + id);
		   					return id.toString();
					    } finally {
					    	fs.close();
						}
					} catch (IOException e) {
						if (log.isErrorEnabled())
							log.error("error reading file " + file.getName(), e);
						throw new UploadActionException("internal error");
			   		} finally {
			   			try { stmt.close(); } catch (final SQLException e) {}
			   		}
				} finally {
					try { conn.close(); } catch (final SQLException e) {}
				}
			}
		} catch (SQLException e) {
			if (log.isErrorEnabled())
				log.error("error uploading file", e);
			throw new UploadActionException("internal error");
		} finally {
			super.removeSessionFileItems(request);
		}
		return null;
	}

	@Override
	public void removeItem(@SuppressWarnings("unused") final HttpServletRequest request, final String fieldName)  throws UploadActionException {
		try {
	    	final Connection conn = db.getConnection();
			try {
				Database.executeUpdate(conn,
"DELETE FROM import_data WHERE field_name=?;", fieldName);
				if (log.isTraceEnabled())
					log.trace("imported data '" + fieldName + "' has been removed");
			} finally {
				try { conn.close(); } catch (final SQLException e) {}
			}
		} catch (Throwable x) {
			if (log.isTraceEnabled())
				log.trace("failed to remove imported data '" + fieldName + "'");
		}
	}

	public static String extractFileExtension(final String fileName) {
		if (fileName == null)
			return null;
		int dotInd = fileName.lastIndexOf('.');
		return (dotInd > 0 && dotInd < fileName.length()) ? fileName.substring(dotInd + 1) : null;
	}

}
