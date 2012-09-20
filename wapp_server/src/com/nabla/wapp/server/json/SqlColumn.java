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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;

import net.minidev.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author nabla64
 *
 */
public class SqlColumn {

	private static final Log					log = LogFactory.getLog(JsonResponse.class);
	private static final SimpleDateFormat		timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private final String	label;
	private final int		type;

	public SqlColumn(String label, int type) {
		if (label.startsWith("b_")) {
			this.label = label.substring(2);
			this.type = Types.BOOLEAN;
		} else if (label.startsWith("i_")) {
			this.label = label.substring(2);
			this.type = Types.INTEGER;
		} else if (label.startsWith("d_")) {
			this.label = label.substring(2);
			this.type = Types.DATE;
		} else if (label.startsWith("s_")) {
			this.label = label.substring(2);
			this.type = Types.VARCHAR;
		} else if (label.startsWith("d_")) {
			this.label = label.substring(2);
			this.type = Types.DOUBLE;
		} else if (label.startsWith("f_")) {
			this.label = label.substring(2);
			this.type = Types.FLOAT;
		} else {
			this.label = label;
			this.type = type;
		}
		if (log.isDebugEnabled()) {
			String s;
			switch (this.type) {
			case Types.BIGINT:
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT:
				s = "INTEGER";
				break;
			case Types.BOOLEAN:
			case Types.BIT:
				s = "BOOLEAN";
				break;
			case Types.DATE:
				s = "DATE";
				break;
			case Types.TIMESTAMP:
				s = "TIMESTAMP";
				break;
			case Types.DOUBLE:
				s = "DOUBLE";
				break;
			case Types.FLOAT:
				s = "FLOAT";
				break;
			case Types.NULL:
				s = "NULL";
				break;
			default:
				s = "STRING";
				break;
			}
			log.debug("column '" + this.label + "' type = " + s);
		}
	}

	public String getLabel() {
		return label;
	}

	public int getType() {
		return type;
	}

	public void write(final ResultSet rs, int column, final JSONObject record) throws SQLException {
		switch (type) {
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
			final Date dt = rs.getDate(column);
			if (rs.wasNull())
				record.put(label, null);
			else
				record.put(label, new JSonDate(dt));
			return;
		case Types.TIMESTAMP:
			final Timestamp tm = rs.getTimestamp(column);
			if (rs.wasNull())
				record.put(label, null);
			else
				record.put(label, timeStampFormat.format(tm));
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
}
