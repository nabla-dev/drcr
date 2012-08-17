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
package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Persist;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlUserList {
	@ElementList(entry="user", inline=true, required=false)
	List<XmlUser>	list;

	@Persist
	public void prepare(Map session) {
		Importer.setContext(session, new HashSet<String>());
	}

	@Commit
	public void commit(Map session) {
		Importer.clearContext(session);
	}

	public void clear(final Connection conn) throws SQLException {
		Database.executeUpdate(conn,
"DELETE FROM user WHERE name NOT LIKE ?;", IRootUser.NAME);
	}

	public boolean save(final Connection conn, final Map<String, Integer> roleIds, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
		int n = errors.size();
		for (XmlUser e : list)
			e.save(conn, roleIds, option, errors);
		return n != errors.size();
	}
}
