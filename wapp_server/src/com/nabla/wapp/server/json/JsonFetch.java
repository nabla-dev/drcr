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
package com.nabla.wapp.server.json;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * The <code></code> object is used to
 *
 */
public class JsonFetch extends LinkedList<IOdbcToJsonEncoder> {

	private static final long	serialVersionUID = 1L;
	private static final Log	log = LogFactory.getLog(JsonFetch.class);

	public JsonFetch() {}

	public JsonFetch(IOdbcToJsonEncoder... encoders) {
		super(Arrays.asList(encoders));
	}

	public String createSql(final AbstractFetch options, final String baseSql) {
		String sql;
		if (options.isRange())
			sql = baseSql.replaceFirst("SELECT", "SELECT SQL_CALC_FOUND_ROWS");
		else
			sql = baseSql;
		final String orderBy = options.getOrderBy();
		if (orderBy != null) {
			if (log.isTraceEnabled())
				log.trace("ORDER BY = " + orderBy);
			final String tmp = sql.replaceFirst("\\{ORDER BY\\}", "ORDER BY " + orderBy);
			if (sql.equals(tmp))
				sql = tmp.replaceFirst("\\{AND ORDER BY\\}", ", " + orderBy);
			else
				sql = tmp;
		} else
			sql = sql.replaceFirst("\\{ORDER BY\\}", "").replaceFirst("\\{AND ORDER BY\\}", "");
		final String where = options.getFilter();
		if (where != null) {
			if (log.isTraceEnabled())
				log.trace("WHERE = " + where);
			final String tmp = sql.replaceFirst("\\{WHERE\\}", "WHERE " + where);
			if (sql.equals(tmp))
				sql = tmp.replaceFirst("\\{AND WHERE\\}", "AND " + where);
			else
				sql = tmp;
		} else {
			sql = sql.replaceFirst("\\{WHERE\\}", "").replaceFirst("\\{AND WHERE\\}", "");
		}
		if (options.isRange())
			sql += " LIMIT ? OFFSET ?";
		sql += ";";
		if (log.isInfoEnabled())
			log.info("SQL: " + sql);
		return sql;
	}

	public FetchResult serialize(final AbstractFetch options, final PreparedStatement stmt) throws SQLException {
		Assert.argumentNotNull(stmt);

		final JsonResponse response = new JsonResponse();
		Integer endRow = null;
		int total = response.putAll(stmt.executeQuery(), this);
		if (options.isRange()) {
			int lastRow = options.getStartRow() + total - 1;
			endRow = (options.getEndRow() < lastRow) ? options.getEndRow() : lastRow;

			final ResultSet rs = stmt.executeQuery("SELECT FOUND_ROWS();");
			rs.next();
			total = rs.getInt(1);
			if (log.isTraceEnabled())
				log.trace("total rows = " + total);
		}
		return new FetchResult(options.getStartRow(), endRow, total, response.toJSONString());
	}

	public FetchResult serialize(final AbstractFetch options, final Connection conn, final String sql, Object... parameters) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn, createSql(options, sql), parameters);
		try {
			int i = (parameters != null) ? parameters.length : 0;
			if (options.isRange()) {
				stmt.setInt(++i, options.getEndRow() - options.getStartRow() + 1);
				stmt.setInt(++i, options.getStartRow());
			}
			return serialize(options, stmt);
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

}
