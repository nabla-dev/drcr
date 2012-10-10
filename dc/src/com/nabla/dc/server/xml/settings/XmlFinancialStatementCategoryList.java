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

import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.SqlInsert;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlFinancialStatementCategoryList {

	@ElementList(entry="financial_statement_category", inline=true, required=false)
	List<XmlFinancialStatementCategory>		list;

	public XmlFinancialStatementCategoryList() {}

	public XmlFinancialStatementCategoryList(final Connection conn) throws SQLException {
		load(conn);
	}

	@Commit
	public void commit(Map session) {
		XmlNode.<ImportContext>getContext(session).getNameList().clear();
	}

	public void clear(final Connection conn) throws SQLException {
		Database.executeUpdate(conn, "DELETE FROM fa_fs_category;");
	}

	public void save(final Connection conn, final SqlInsertOptions option) throws SQLException, InternalErrorException {
		if (list != null && !list.isEmpty()) {
			final SqlInsert<XmlFinancialStatementCategory> sql = new SqlInsert<XmlFinancialStatementCategory>(XmlFinancialStatementCategory.class, option);
			final BatchInsertStatement<XmlFinancialStatementCategory> batch = new BatchInsertStatement<XmlFinancialStatementCategory>(conn, sql);
			try {
				for (XmlFinancialStatementCategory e : list)
					batch.add(e);
				batch.execute();
			} finally {
				batch.close();
			}
		}
	}

	public void load(final Connection conn) throws SQLException {
		final Statement stmt = conn.createStatement();
		try {
			final ResultSet rs = stmt.executeQuery(
"SELECT name, active FROM fa_fs_category WHERE uname IS NOT NULL;");
			try {
				if (rs.next()) {
					list = new LinkedList<XmlFinancialStatementCategory>();
					do {
						list.add(new XmlFinancialStatementCategory(rs));
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
