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
package com.nabla.dc.server.handler.fixed_asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.nabla.dc.shared.command.fixed_asset.UpdateCompanyFixedAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class UpdateCompanyFixedAssetCategoryHandler extends AbstractHandler<UpdateCompanyFixedAssetCategory, StringResult> {

	public UpdateCompanyFixedAssetCategoryHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final UpdateCompanyFixedAssetCategory cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			Database.executeUpdate(conn,
"UPDATE fa_company_asset_category SET fa_fs_category_id=NULL WHERE company_id=?;",
					cmd.getCompanyId());
			final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO fa_company_asset_category (company_id,fa_asset_category_id,fa_fs_category_id,active) VALUES(?,?,?,?)" +
" ON DUPLICATE KEY UPDATE fa_fs_category_id=VALUES(fa_fs_category_id),active=VALUES(active);");
			try {
				stmt.setInt(1, cmd.getCompanyId());
				for (Map.Entry<Integer, Map<Integer,Boolean>> fsCategory : cmd.getCategories().entrySet()) {
					stmt.setInt(3, fsCategory.getKey());
					for (Map.Entry<Integer,Boolean> assetCategory : fsCategory.getValue().entrySet()) {
						stmt.setInt(2, assetCategory.getKey());
						stmt.setBoolean(4, assetCategory.getValue());
						stmt.addBatch();
					}
				}
				if (!Database.isBatchCompleted(stmt.executeBatch()))
					Util.throwInternalErrorException("failed to save company asset categories");
			} finally {
				Database.close(stmt);
			}
			guard.setSuccess();
			return null;
		} finally {
			guard.close();
		}
	}

}
