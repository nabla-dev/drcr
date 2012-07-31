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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author nabla
 *
 */
public class ImportErrorManager implements ICsvErrorList {

	private static final int		MAX_ERROR_MESSAGE = 255;
	private static final int		MAX_FIELD_NAME = 32;
	private static final int		DEFAULT_MAX_ERRORS = 100;

	private static final int		COL_ID = 1;
	private static final int		COL_LINE_NO = 2;
	private static final int		COL_FIELD = 3;
	private static final int		COL_ERROR = 4;

	private static final Log		log = LogFactory.getLog(ImportErrorManager.class);
	private final Connection		conn;
	private PreparedStatement		stmt;
	private final Integer			batchId;
	private int					line;
	private int						maxErrors;
	private int					size;

	/**
	 * Constructor
	 * @param writeDb - database
	 * @param batchId - import batch ID
	 * @param maxErrors - maximum number of errors (default is 100)
	 * @throws SQLException
	 */
	public ImportErrorManager(final IDatabase writeDb, final Integer batchId, final int maxErrors) throws SQLException {
		Assert.argumentNotNull(writeDb);
		Assert.argument(maxErrors > 0);

		this.conn = writeDb.getConnection();
		this.batchId = batchId;
		line = 1;
		this.maxErrors = maxErrors;
		this.size = 0;
	}

	/**
	 * Constructor that uses the default maximum number of errors (100)
	 * @param writeDb - database
	 * @param batchId - import batch ID
	 * @throws SQLException
	 */
	public ImportErrorManager(final IDatabase writeDb, final Integer batchId) throws SQLException {
		this(writeDb, batchId, DEFAULT_MAX_ERRORS);
	}

	/**
	 * To be called in your <b>finally</b> clause
	 */
	public void close() {
		if (stmt != null) {
			Database.close(stmt);
			stmt = null;
		}
		Database.close(conn);
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Check if any errors have been logged
	 * @return true if no errors have been logged, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return stmt == null;
	}

	/**
	 * Check if maximum number of errors has been reached
	 * @return true if maximum number of errors has been reached, false otherwise
	 */
	@Override
	public boolean isFull() {
		return size >= maxErrors;
	}

	/**
	 * Set current line number
	 * @param lineNo - line number of error
	 * */
	@Override
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * Get current line number
	 * @return line number of error
	 * */
	@Override
	public int getLine() {
		return line;
	}

	/**
	 * Add error
	 * @param fieldName - field name
	 * @param error - error message
	 * @throws SQLException
	 */
	@Override
	public void add(String fieldName, String error) {
		if (!isFull()) {
			if (fieldName != null && fieldName.length() > MAX_FIELD_NAME)
				fieldName = fieldName.substring(0, MAX_FIELD_NAME);
			if (error == null || error.isEmpty())
				error = CommonServerErrors.INTERNAL_ERROR.toString();
			if (error.length() > MAX_ERROR_MESSAGE)
				error = error.substring(0, MAX_ERROR_MESSAGE);
			if (log.isTraceEnabled())
				log.trace("[" + getLine() + "] " + fieldName + ":" + error);
			try {
				if (stmt == null) {
					Database.executeUpdate(conn,
"DELETE FROM import_error WHERE import_data_id=?;", batchId);
					stmt = conn.prepareStatement(
"INSERT INTO import_error(import_data_id,line_no,field,error) VALUES(?,?,?,?);");
					stmt.setInt(COL_ID, batchId);
				}
				stmt.setInt(COL_LINE_NO, getLine());
				if (fieldName == null)
					stmt.setNull(COL_FIELD, Types.VARCHAR);
				else
					stmt.setString(COL_FIELD, fieldName);
				++size;
				stmt.setString(COL_ERROR, (size < maxErrors) ? error : ServerErrors.TOO_MANY_ERRORS.toString());
				stmt.executeUpdate();
			} catch (SQLException e) {
				if (log.isErrorEnabled())
					log.error("failed to save import error to database", e);
				size = maxErrors;
			}
		}
	}

	@Override
	public void add(final String error) {
		add(null, error);
	}

	@Override
	public <E extends Enum<E>> void add(final String columnName, final E error) {
		add(columnName, error.toString());
	}

	@Override
	public <E extends Enum<E>> void add(final E error) {
		add(null, error);
	}

}
