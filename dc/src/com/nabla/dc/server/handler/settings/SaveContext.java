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
package com.nabla.dc.server.handler.settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.shared.database.SqlInsertOptions;

/**
 * @author nabla64
 *
 */
public class SaveContext {

	private final Map<String, Integer>		privilegeIds;
	private final Map<String, Integer>		roleIds;
	private final Map<String, Integer>		userIds;
	private final SqlInsertOptions			option;
	private final ICsvErrorList			errors;

	public SaveContext(final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException {
		privilegeIds = getIdList(conn, "SELECT id, name FROM role WHERE uname IS NOT NULL AND privilege=TRUE;");
		roleIds = getIdList(conn, "SELECT id, name FROM role WHERE uname IS NOT NULL AND privilege=FALSE;");
		userIds = getIdList(conn, "SELECT id, name FROM user WHERE uname IS NOT NULL;");
		this.option = option;
		this.errors = errors;
	}

	public static Map<String, Integer> getIdList(final Connection conn, final String sql) throws SQLException {
		final Map<String, Integer> ids = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
		final Statement stmt = conn.createStatement();
		try {
			final ResultSet rs = stmt.executeQuery(sql);
			try {
				while (rs.next())
					ids.put(rs.getString(2), rs.getInt(1));
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		return ids;
	}

	public Map<String, Integer> getPrivilegeIds() {
		return privilegeIds;
	}

	public Map<String, Integer> getRoleIds() {
		return roleIds;
	}

	public Map<String, Integer> getUserIds() {
		return userIds;
	}

	public SqlInsertOptions getOption() {
		return option;
	}

	public ICsvErrorList getErrors() {
		return errors;
	}
}
