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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlCompanyUserList {
	@ElementList(entry="user", inline=true, required=false)
	List<XmlCompanyUser>	list;

	public XmlCompanyUserList() {}

	public XmlCompanyUserList(final Connection conn, final Integer companyId) throws SQLException {
		load(conn, companyId);
	}

	@Commit
	public void commit(Map session) {
		XmlNode.<ImportContext>getContext(session).getNameList().clear();
	}

	public void clear(final Connection conn, final Integer companyId) throws SQLException {
		Database.executeUpdate(conn,
"DELETE FROM company_user WHERE company_id=?;", 	companyId);
		Database.executeUpdate(conn,
"DELETE FROM user_definition WHERE object_id=?;", companyId);
	}

	public boolean save(final Connection conn, final Integer companyId, final SaveContext ctx) throws SQLException, DispatchException {
		boolean success = true;
		if (list != null) {
			for (XmlCompanyUser e : list)
				success = e.save(conn, companyId, ctx) && success;
		}
		return success;
	}

	public void load(final Connection conn, final Integer companyId) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT u.id, u.name" +
" FROM company_user AS c INNER JOIN user AS u ON c.user_id=u.id" +
" WHERE c.company_id=?;",
companyId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (rs.next()) {
					list = new LinkedList<XmlCompanyUser>();
					do {
						list.add(new XmlCompanyUser(companyId, rs));
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
