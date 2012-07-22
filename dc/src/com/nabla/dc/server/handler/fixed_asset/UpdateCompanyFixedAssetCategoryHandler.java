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
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.fixed_asset.UpdateCompanyFixedAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateCompanyFixedAssetCategoryHandler extends AbstractHandler<UpdateCompanyFixedAssetCategory, StringResult> {

//	private final SqlInsert<UpdateCompanyFixedAssetCategory> sql = new SqlInsert<UpdateCompanyFixedAssetCategory>(UpdateCompanyFixedAssetCategory.class, SqlInsertOptions.OVERWRITE);

	public UpdateCompanyFixedAssetCategoryHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final UpdateCompanyFixedAssetCategory record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (record.getBalanceSheetCategory() != null)
			record.setBalanceSheetCategoryId(getBalanceSheetCategoryId(record.getBalanceSheetCategory(), ctx.getReadConnection()));

		return null;
	}

	private int getBalanceSheetCategoryId(final String name, final Connection conn) throws ValidationException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT content FROM import_data WHERE id=?;", name);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (rs.next())
					return rs.getInt(1);
				throw new ValidationException("bs_category", ServerErrors.UNDEFINED_BS_CATEGORY);
			} finally {
				Database.close(rs);
			}
		} finally {
			Database.close(stmt);
		}
	}
}
