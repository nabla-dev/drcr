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
package com.nabla.dc.server.xml.assets;

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
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author FNorais
 *
 */
@Root
public class XmlAssetList {

	private static final Log	log = LogFactory.getLog(XmlAssetList.class);

	@ElementList(entry="asset", inline=true, required=false)
	List<XmlAsset>	list;

	public XmlAssetList() {}
/*
	public XmlAssetList(final Connection conn, final Integer companyId) throws SQLException {
		load(conn, companyId);
	}
*/
	@Commit
	public void commit(Map session) {
		final ImportContext ctx = XmlNode.getContext(session);
		ctx.getNameList().clear();
		if (log.isDebugEnabled())
			log.debug("clearing name and code list for company accounts");
	}

	public void postValidate(final ImportContext.Company company, final IErrorList<Integer> errors) throws DispatchException {
		if (list != null) {
			for (XmlAsset e : list)
				e.postValidate(company, errors);
		}
	}

	public void clear(final Connection conn) throws SQLException {
		Database.executeUpdate(conn,
"DELETE FROM account WHERE company_id=?;", companyId);
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		if (list != null && !list.isEmpty()) {
			final SqlInsert<XmlAsset> sql = new SqlInsert<XmlAsset>(XmlAsset.class);
			final BatchInsertStatement<XmlAsset> batch = new BatchInsertStatement<XmlAsset>(conn, sql);
			try {
				for (XmlAsset e : list)
					batch.add(e);
				batch.execute();
			} finally {
				batch.close();
			}
		}
		return true;
	}
/*
	public void load(final Connection conn) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT code, name, active, cost_centre, department, balance_sheet" +
" FROM account" +
" WHERE company_id=? AND uname IS NOT NULL;",
companyId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (rs.next()) {
					list = new LinkedList<XmlAsset>();
					do {
						list.add(new XmlAsset(rs));
					} while (rs.next());
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}*/
}
