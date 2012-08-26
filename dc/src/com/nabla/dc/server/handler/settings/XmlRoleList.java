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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlRoleList {
	@ElementList(entry="role", inline=true, required=false)
	List<XmlRole>	list;

	@Commit
	public void commit(Map session) {
		XmlNode.<ImportContext>getContext(session).getNameList().clear();
	}

	public void clear(final Connection conn) throws SQLException {
		Database.executeUpdate(conn, "DELETE FROM role WHERE internal=FALSE;");
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		boolean success = true;
		if (list != null) {
			for (XmlRole e : list)
				success = e.save(conn, ctx) && success;
		}
		return success;
	}
}
