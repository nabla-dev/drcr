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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.SqlInsert;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlAccountList {
	private static final Log	log = LogFactory.getLog(XmlAccountList.class);

	@ElementList(entry="account", inline=true, required=false)
	List<XmlAccount>	list;

	@Commit
	public void commit(Map session) {
		final ImportContext ctx = XmlNode.getContext(session);
		ctx.getNameList().clear();
		ctx.getAccountCodeList().clear();
		if (log.isDebugEnabled())
			log.debug("clearing name and code list for company accounts");
	}

	public void clear(final Connection conn, final Integer companyId) throws SQLException {
		Database.executeUpdate(conn,
"DELETE FROM account WHERE company_id=?;", companyId);
	}

	public void save(final Connection conn, final Integer companyId) throws SQLException, DispatchException {
		if (list != null && !list.isEmpty()) {
			final SqlInsert<XmlAccount> sql = new SqlInsert<XmlAccount>(XmlAccount.class);
			final BatchInsertStatement<XmlAccount> batch = new BatchInsertStatement<XmlAccount>(conn, sql);
			try {
				for (XmlAccount e : list) {
					e.setCompanyId(companyId);
					batch.add(e);
				}
				batch.execute();
			} finally {
				batch.close();
			}
		}
	}
}
