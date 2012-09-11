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
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class JsonResponse extends JSONArray {

	private static final long 				serialVersionUID = 1L;
	private static final SimpleDateFormat	format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public int putAll(final ResultSet rs, final int[] columnTypes) throws SQLException {
		Assert.argumentNotNull(rs);

		while (rs.next())
			add(writeRecord(rs, columnTypes));
		return size();
	}

	public int putAll(final ResultSet rs) throws SQLException {
		if (rs.next()) {
			final int[] columnTypes = getColumnTypes(rs);
			do {
				add(writeRecord(rs, columnTypes));
			} while (rs.next());
		}
		return size();
	}

	public boolean putNext(final ResultSet rs, final int[] columnTypes) throws SQLException {
		Assert.argumentNotNull(rs);

		if (!rs.next())
			return false;
		add(writeRecord(rs, columnTypes));
		return true;
	}

	public boolean putNext(final ResultSet rs) throws SQLException {
		Assert.argumentNotNull(rs);

		if (!rs.next())
			return false;
		add(writeRecord(rs, getColumnTypes(rs)));
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

	public static int[] getColumnTypes(final ResultSet rs) throws SQLException {
		Assert.argumentNotNull(rs);

		final ResultSetMetaData header = rs.getMetaData();
		int columnCount = header.getColumnCount();
		final int[] types = new int[columnCount];
		for (int c = 1; c <= columnCount; ++c)
			types[c-1] = isColumnBoolean(header.getColumnLabel(c)) ? Types.BOOLEAN : header.getColumnType(c);
		return types;
	}

	public static boolean isColumnBoolean(final String label) {
		if (label.startsWith("is"))
			return label.length() > 2 && Character.isUpperCase(label.charAt(2));
		if (label.startsWith("can") || label.startsWith("has"))
			return label.length() > 3 && Character.isUpperCase(label.charAt(3));
		return false;
	}

	private static JSONObject writeRecord(final ResultSet rs, final int[] columnTypes) throws SQLException {
		Assert.argumentNotNull(rs);
		Assert.argumentNotNull(columnTypes);

		final JSONObject record = new JSONObject();
		final ResultSetMetaData header = rs.getMetaData();
		int columnCount = header.getColumnCount();
		for (int i = 1; i <= columnCount; ++i)
			writeColumn(rs, i, columnTypes[i-1], record, header.getColumnLabel(i));
		return record;
	}

	private static void writeColumn(final ResultSet rs, int column, int columnType, final JSONObject record, final String label) throws SQLException {
		switch (columnType) {
		case Types.BIGINT:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
			record.put(label, rs.getInt(column));
			break;
		case Types.BOOLEAN:
		case Types.BIT:
			record.put(label, rs.getBoolean(column));
			break;
		case Types.DATE:
			record.put(label, rs.getDate(column));
			break;
		case Types.TIMESTAMP:
			final Timestamp tm = rs.getTimestamp(column);
			if (rs.wasNull())
				record.put(label, null);
			else
				record.put(label, format.format(tm));
			return;
		case Types.DOUBLE:
			record.put(label, rs.getDouble(column));
			break;
		case Types.FLOAT:
			record.put(label, rs.getFloat(column));
			break;
		case Types.NULL:
			record.put(label, null);
			return;
		default:
			record.put(label, rs.getString(column));
			break;
		}
		if (rs.wasNull())
			record.put(label, null);
	}

	public StringResult toStringResult() {
		return new StringResult(toJSONString());
	}

}
