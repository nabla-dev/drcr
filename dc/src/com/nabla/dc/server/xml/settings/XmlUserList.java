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
package com.nabla.dc.server.xml.settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlUserList {

	@ElementList(entry="user", inline=true, required=false)
	List<XmlUser>	list;

	public XmlUserList() {}

	public XmlUserList(final Connection conn) throws SQLException {
		load(conn);
	}

	@Commit
	public void commit(Map session) {
		XmlNode.<ImportContext>getContext(session).getNameList().clear();
	}

	public void clear(final Connection conn) throws SQLException {
		Database.executeUpdate(conn,
"DELETE FROM user WHERE name NOT LIKE ?;", IRootUser.NAME);
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		boolean success = true;
		if (list != null) {
			for (XmlUser e : list)
				success = e.save(conn, ctx) && success;
		}
		return success;
	}

	public void load(final Connection conn) throws SQLException {
		final Statement stmt = conn.createStatement();
		try {
			final ResultSet rs = stmt.executeQuery(
"SELECT id, name, active FROM user WHERE uname IS NOT NULL AND uname NOT LIKE 'root';");
			try {
				if (rs.next()) {
					list = new LinkedList<XmlUser>();
					do {
						list.add(new XmlUser(rs));
					} while (rs.next());
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}
}
