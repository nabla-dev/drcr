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

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

public class Importer<C extends IImportContext> {

	private static final Log			log = LogFactory.getLog(Importer.class);
	public static final String		DEFAULT_SQL = "SELECT content, userSessionId FROM import_data WHERE id=?;";

	private final Connection			conn;
	private final String				sql;
	private final IErrorList<Integer>	errors;
	private final Persister			impl;

	public <T> Importer(final Connection conn, final String sql, final C ctx) {
		this.conn = conn;
		this.sql = sql;
		this.errors = ctx.getErrorList();
		impl = new Persister(new ImportVisitorStrategy<C>(ctx), new SimpleMatcher());
	}

	public <T> Importer(final Connection conn, final C ctx) {
		this(conn, DEFAULT_SQL, ctx);
	}

	public <T> T read(final Class<T> clazz, final Integer dataId, final String userSessionId)  throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, sql, dataId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next()) {
					errors.add(CommonServerErrors.NO_DATA);
					return null;
				}
				if (!userSessionId.equals(rs.getString("userSessionId"))) {
					if (log.isTraceEnabled())
						log.trace("invalid user session ID");
					errors.add(CommonServerErrors.ACCESS_DENIED);
					return null;
				}
				try {
					return impl.read(clazz, rs.getBinaryStream("content"));
				} catch (final InvocationTargetException e) {
					if (log.isDebugEnabled())
						log.debug("exception thrown from a validate(). assume error was added to list", e);
				} catch (final ValueRequiredException e) {
					if (log.isDebugEnabled())
						log.debug("required value", e);
					errors.add(Util.extractLine(e), Util.extractFieldName(e), CommonServerErrors.REQUIRED_VALUE);
				} catch (final ElementException e) {
					errors.add(Util.extractFieldName(e), e.getLocalizedMessage());
				} catch (final PersistenceException e) {
					if (log.isDebugEnabled())
						log.debug("deserialization error", e);
					errors.add(Util.extractLine(e), Util.extractFieldName(e), CommonServerErrors.INVALID_VALUE);
				} catch (final XMLStreamException e) {
					if (log.isDebugEnabled())
						log.debug("XML error", e);
					errors.add(Util.extractLine(e), e.getLocalizedMessage());
				} catch (final Exception e) {
					if (log.isDebugEnabled())
						log.debug("error", e);
					errors.add(Util.extractLine(e), e.getLocalizedMessage());
				}
				return null;
			} finally {
				Database.close(rs);
			}
		} finally {
			Database.close(stmt);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getContext(final Map session) {
		return (T)session.get(ImportVisitorStrategy.KEY_CTX);
	}
}
