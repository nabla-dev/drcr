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
package com.nabla.wapp.server.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.general.StringSet;

/**
 * @author nabla
 *
 */
public class StringSetSetter implements IStatementSetter {

	@Override
	public int prepare(StringBuilder sql, int parameterIndex, Object value) {
		if (value == null)
			return 1;
		final StringSet values = (StringSet)value;
		int count = values.size();
		if (count < 2)
			return 1;
		// replace '?' with '?,?,?...'
		final StringBuilder buffer = new StringBuilder(",?");
		for (int i = 2; i < count; ++i)
			buffer.append(",?");
		int offset = sql.indexOf("?");
		Assert.state(offset != -1);
		while (--parameterIndex > 0) {
			offset = sql.indexOf("?", offset + 1);
			Assert.state(offset != -1);
		}
		sql.insert(offset + 1, buffer.toString());
		return count;
	}

	@Override
	public int setValue(PreparedStatement stmt, int parameterIndex, Object value) throws SQLException {
		if (value == null) {
			stmt.setNull(parameterIndex, Types.VARCHAR);
			return 1;
		}
		final StringSet values = (StringSet)value;
		if (values.isEmpty()) {
			stmt.setNull(parameterIndex, Types.VARCHAR);
			return 1;
		}
		for (String val : values)
			stmt.setString(parameterIndex++, val);
		return values.size();
	}

}
