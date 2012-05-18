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
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class JsonResponse extends JSONArray {

	private static final long 	serialVersionUID = 1L;

	public int putAll(final ResultSet rs, final List<IOdbcToJsonEncoder> encoders) throws SQLException {
		Assert.argumentNotNull(rs);

		while (rs.next())
			add(writeRecord(rs, encoders));
		return size();
	}
/*
	public int putAll(final ResultSet rs) throws SQLException {
		return putAll(rs, getEncoderList(rs));
	}
*/
	public boolean putNext(final ResultSet rs, final List<IOdbcToJsonEncoder> encoders) throws SQLException {
		Assert.argumentNotNull(rs);

		if (!rs.next())
			return false;
		add(writeRecord(rs, encoders));
		return true;
	}
/*
	public boolean putNext(final ResultSet rs) throws SQLException {
		return putNext(rs, getEncoderList(rs));
	}
*/
	public void put(final Integer value) {
		put("id", value);
	}

	public void put(final String field, final Integer value) {
		final JSONObject record = new JSONObject();
		record.put(field, value);
		add(record);
	}

	public static List<IOdbcToJsonEncoder> getEncoderList(final ResultSet rs) throws SQLException {
		final List<IOdbcToJsonEncoder> encoders = new LinkedList<IOdbcToJsonEncoder>();
		final ResultSetMetaData header = rs.getMetaData();
		for (int c = 1; c <= header.getColumnCount(); ++c) {
			final String label = header.getColumnLabel(c);
			if (label.startsWith("is") || label.startsWith("can"))	// assume if label starts with "is" then it's a boolean
				encoders.add(new OdbcBooleanToJson(label));
			else {
				switch (header.getColumnType(c)) {
				case Types.BIGINT:
				case Types.INTEGER:
				case Types.SMALLINT:
				case Types.TINYINT:
					encoders.add(new OdbcIntToJson(label));
					break;
				case Types.BOOLEAN:
					encoders.add(new OdbcBooleanToJson(label));
					break;
				case Types.DATE:
					encoders.add(new OdbcDateToJson(label));
					break;
				case Types.TIMESTAMP:
					encoders.add(new OdbcTimeStampToJson(label));
					break;
				case Types.DOUBLE:
					encoders.add(new OdbcDoubleToJson(label));
					break;
				case Types.FLOAT:
					encoders.add(new OdbcFloatToJson(label));
					break;
				case Types.NULL:
					encoders.add(new OdbcNullToJson(label));
					break;
				default:
					encoders.add(new OdbcStringToJson(label));
					break;
				}
			}
		}
		return encoders;
	}

	private static JSONObject writeRecord(final ResultSet rs, final List<IOdbcToJsonEncoder> encoders) throws SQLException {
		Assert.argumentNotNull(rs);
		Assert.argumentNotNull(encoders);

		final JSONObject record = new JSONObject();
		for (int i = 0; i < encoders.size(); ++i)
			encoders.get(i).encode(rs, i + 1, record);
		return record;
	}

	public StringResult toStringResult() {
		return new StringResult(toJSONString());
	}

}
