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
package com.nabla.wapp.server.xml;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.ValueRequiredException;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla64
 *
 */
public class Importer extends Persister {

	public static final String	DEFAULT_SQL = "SELECT content FROM import_data WHERE id=?;";
	private static final Log		log = LogFactory.getLog(Importer.class);

	private final Connection		conn;
	private final String			sql;
	private final ICsvErrorList	errors;

	public Importer(final Connection conn, final String sql, final ICsvErrorList errors) {
		super(new SimpleMatcher());
		this.conn = conn;
		this.sql = sql;
		this.errors = errors;
	}

	public Importer(final Connection conn, final ICsvErrorList errors) {
		this(conn, DEFAULT_SQL, errors);
	}

	public <T> T read(final Class<T> clazz, final Integer dataId)  throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, sql, dataId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next()) {
					errors.add(CommonServerErrors.NO_DATA);
					return null;
				}
				try {
					return super.read(clazz, rs.getBinaryStream("content"));
				} catch (final InvocationTargetException e) {
					if (log.isDebugEnabled())
						log.debug("reflection wrapped an exception thrown during deserialization");
					final Throwable ee = e.getCause();
					if (ee == null) {
						if (log.isErrorEnabled())
							log.error("error while deserializing xml request", e);
						errors.add(CommonServerErrors.INTERNAL_ERROR);
					} else if (ee.getClass().equals(ValidationException.class)) {
						final Map.Entry<String, String> error = ((ValidationException)ee).getError();
						errors.add(error.getKey(), error.getValue());
					} else {
						if (log.isErrorEnabled())
							log.error("error while deserializing xml request", ee);
						errors.add(ee.getLocalizedMessage());
					}
				} catch (final ValueRequiredException e) {
					if (log.isDebugEnabled())
						log.debug("required value", e);
					errors.setLine(Util.extractLine(e));
					errors.add(Util.extractFieldName(e), CommonServerErrors.REQUIRED_VALUE);
				} catch (final ElementException e) {
					if (log.isDebugEnabled())
						log.debug("required value", e);
					errors.setLine(Util.extractLine(e));
					errors.add(Util.extractFieldName(e), e.getLocalizedMessage());
				} catch (final PersistenceException e) {
					if (log.isDebugEnabled())
						log.debug("deserialization error", e);
					errors.setLine(Util.extractLine(e));
					errors.add(Util.extractFieldName(e), CommonServerErrors.INVALID_VALUE);
				} catch (final XMLStreamException e) {
					if (log.isDebugEnabled())
						log.debug("XML error", e);
					errors.setLine(Util.extractLine(e));
					errors.add(e.getLocalizedMessage());
				} catch (final Exception e) {
					if (log.isDebugEnabled())
						log.debug("error", e);
					errors.add(e.getLocalizedMessage());
				}
				return null;
			} finally {
				Database.close(rs);
			}
		} finally {
			Database.close(stmt);
		}
	}

}
