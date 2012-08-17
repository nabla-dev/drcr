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
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

/**
 * @author nabla64
 *
 */
public class Importer {

	private static final Log		log = LogFactory.getLog(Importer.class);
	private static final String	CTX_KEY = "ctx";
	private static final String	ERROR_LIST_KEY = "errors";
	public static final String	DEFAULT_SQL = "SELECT content FROM import_data WHERE id=?;";

	static class MyVisitorStrategy extends VisitorStrategy {

		private boolean				sessionInitialized = false;
		private final ICsvErrorList	errors;
		private Object					ctx;

		public MyVisitorStrategy(Object ctx, final ICsvErrorList errors) {
			super(null);
			Assert.argumentNotNull(errors);
			this.errors = errors;
			this.ctx = ctx;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Value read(Type type, NodeMap<InputNode> node, Map session) throws Exception {
			if (!sessionInitialized) {
				if (ctx != null)
					session.put(CTX_KEY, ctx);
				session.put(ERROR_LIST_KEY, errors);
				sessionInitialized = true;
			}
			errors.setLine(node.getNode().getPosition().getLine());
			return super.read(type, node, session);
		}

	}

	private final Connection		conn;
	private final String			sql;
	private final ICsvErrorList	errors;
	private final Persister		impl;

	public Importer(final Connection conn, final String sql, Object ctx, final ICsvErrorList errors) {
		this.conn = conn;
		this.sql = sql;
		this.errors = errors;
		impl = new Persister(new MyVisitorStrategy(ctx, errors), new SimpleMatcher());
	}

	public Importer(final Connection conn, Object ctx, final ICsvErrorList errors) {
		this(conn, DEFAULT_SQL, ctx, errors);
	}

	public Importer(final Connection conn, final ICsvErrorList errors) {
		this(conn, null, errors);
	}

	public static ICsvErrorList getErrors(Map session) {
		if (session == null)
			return null;
		return (ICsvErrorList)session.get(ERROR_LIST_KEY);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getContext(Map session) {
		if (session == null)
			return null;
		return (T) session.get(CTX_KEY);
	}

	@SuppressWarnings("unchecked")
	public static <T> void setContext(Map session, T ctx) {
		Assert.argumentNotNull(session);
		session.put(CTX_KEY, ctx);
	}

	public static void clearContext(Map session) {
		session.remove(CTX_KEY);
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
					return impl.read(clazz, rs.getBinaryStream("content"));
				} catch (final InvocationTargetException e) {
					if (log.isDebugEnabled())
						log.debug("exception thrown from a validate(). assume error was added to list");
				} catch (final ValueRequiredException e) {
					if (log.isDebugEnabled())
						log.debug("required value", e);
					extractCurrentLine(e);
					errors.add(Util.extractFieldName(e), CommonServerErrors.REQUIRED_VALUE);
				} catch (final ElementException e) {
					extractCurrentLine(e);
					errors.add(Util.extractFieldName(e), e.getLocalizedMessage());
				} catch (final PersistenceException e) {
					if (log.isDebugEnabled())
						log.debug("deserialization error", e);
					extractCurrentLine(e);
					errors.add(Util.extractFieldName(e), CommonServerErrors.INVALID_VALUE);
				} catch (final XMLStreamException e) {
					if (log.isDebugEnabled())
						log.debug("XML error", e);
					extractCurrentLine(e);
					errors.add(e.getLocalizedMessage());
				} catch (final Exception e) {
					if (log.isDebugEnabled())
						log.debug("error", e);
					extractCurrentLine(e);
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

	protected void extractCurrentLine(final Exception e) {
		final Integer value = Util.extractLine(e);
		if (value != null)
			errors.setLine(value);
	}
}
