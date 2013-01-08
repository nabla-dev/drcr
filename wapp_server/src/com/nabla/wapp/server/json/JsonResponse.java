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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.dispatch.StringResult;


public class JsonResponse extends JSONArray {

	private static final long 	serialVersionUID = 1L;

	public int putAll(final ResultSet rs) throws SQLException {
		if (rs.next()) {
			final List<SqlColumn> columns = getColumns(rs);
			do {
				add(writeRecord(rs, columns));
			} while (rs.next());
		}
		return size();
	}

	public boolean putNext(final ResultSet rs, final List<SqlColumn> columns) throws SQLException {
		Assert.argumentNotNull(rs);

		if (!rs.next())
			return false;
		add(writeRecord(rs, columns));
		return true;
	}

	public boolean putNext(final ResultSet rs) throws SQLException {
		Assert.argumentNotNull(rs);

		if (!rs.next())
			return false;
		add(writeRecord(rs, getColumns(rs)));
		return true;
	}

	public void putId(final Integer value) {
		put("id", value);
	}

	public void put(final String field, final Integer value) {
		final JSONObject record = new JSONObject();
		record.put(field, value);
		add(record);
	}

	public void put(final String field, final Boolean value) {
		final JSONObject record = new JSONObject();
		record.put(field, value);
		add(record);
	}

	public static List<SqlColumn> getColumns(final ResultSet rs) throws SQLException {
		final List<SqlColumn> columns = new LinkedList<SqlColumn>();
		final ResultSetMetaData header = rs.getMetaData();
		int columnCount = header.getColumnCount();
		for (int c = 1; c <= columnCount; ++c)
			columns.add(new SqlColumn(header.getColumnLabel(c), header.getColumnType(c), header.getColumnDisplaySize(c)));
		return columns;
	}

	private static JSONObject writeRecord(final ResultSet rs, final List<SqlColumn> columns) throws SQLException {
		final JSONObject record = new JSONObject();
		int i = 1;
		for (SqlColumn c : columns)
			c.write(rs, i++, record);
		return record;
	}

	public StringResult toStringResult() {
		return new StringResult(toJSONString());
	}

}
