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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.dc.shared.command.fixed_asset.UpdateAssetField;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;


public class UpdateAssetFieldHandler extends AbstractUpdateHandler<UpdateAssetField> {

	public static final UpdateStatement<UpdateAssetField>	sql = new UpdateStatement<UpdateAssetField>(UpdateAssetField.class);

	@Override
	protected void update(final UpdateAssetField record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (record.getDepreciationPeriod() != null) {
			try {
				final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT t.min_depreciation_period, t.max_depreciation_period" +
" FROM fa_asset_category AS t INNER JOIN (" +
	"fa_company_asset_category AS r INNER JOIN fa_asset AS a ON a.fa_company_asset_category_id=r.id" +
") ON r.fa_asset_category_id=t.id" +
" WHERE a.id=?;", record.getId());
				try {
					final ResultSet rs = stmt.executeQuery();
					try {
						if (!rs.next())
							throw new ValidationException(IAsset.NAME, CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
						if (rs.getInt("min_depreciation_period") > record.getDepreciationPeriod() || rs.getInt("max_depreciation_period") < record.getDepreciationPeriod())
							throw new ValidationException(IAsset.DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
					} finally {
						rs.close();
					}
				} finally {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new InternalErrorException(Util.formatInternalErrorDescription(e));
			}
		}
		sql.execute(ctx.getConnection(), record);
	}

}
