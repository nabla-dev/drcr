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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author FNorais
 *
 */
@Root
public class XmlCompanyAssetCategoryList {

	@ElementList(entry="asset_category", inline=true, required=false)
	List<XmlCompanyAssetCategory>	list;

	@Commit
	public void commit(Map session) {
		XmlNode.<ImportContext>getContext(session).getNameList().clear();
	}

	public void clear(final Connection conn, final Integer companyId) throws SQLException {
		Database.executeUpdate(conn,
"DELETE FROM fa_company_asset_category WHERE company_id=?;", companyId);
	}

	public boolean save(final Connection conn, final Integer companyId, final SaveContext ctx) throws SQLException, DispatchException {
		final Map<String, Integer> assetCategories = SaveContext.getIdList(conn,
"SELECT id, name FROM fa_asset_category WHERE uname IS NOT NULL;");
		final Map<String, Integer> fsCategories = SaveContext.getIdList(conn,
"SELECT id, name FROM fa_fs_category WHERE uname IS NOT NULL;");
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO fa_company_asset_category (company_id, fa_asset_category_id, fa_fs_category_id) VALUES(?,?,?);");
		final ICsvErrorList errors = ctx.getErrors();
		try {
			stmt.setInt(1, companyId);
			boolean success = true;
			for (XmlCompanyAssetCategory e : list) {
				Integer id = assetCategories.get(e.getAssetCategory());
				if (id == null) {
					errors.setLine(e.getRow());
					errors.add("asset_category", ServerErrors.UNDEFINED_ASSET_CATEGORY);
					success = false;
					continue;
				}
				stmt.setInt(2, id);
				id = fsCategories.get(e.getFinancialStatementCategory());
				if (id == null) {
					errors.setLine(e.getRow());
					errors.add("financial_statement_category", ServerErrors.UNDEFINED_FS_CATEGORY);
					success = false;
					continue;
				}
				stmt.setInt(3, id);
				stmt.addBatch();
			}
			if (success && !Database.isBatchCompleted(stmt.executeBatch()))
				Util.throwInternalErrorException("failed to insert company asset categories");
			return success;
		} finally {
			stmt.close();
		}
	}

}
