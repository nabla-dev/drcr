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

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.fixed_asset.UpdateFixedAssetCategory;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractUpdateHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateFixedAssetCategoryHandler extends AbstractUpdateHandler<UpdateFixedAssetCategory> {

	private static final UpdateStatement<UpdateFixedAssetCategory>	sql = new UpdateStatement<UpdateFixedAssetCategory>(UpdateFixedAssetCategory.class);

	@Override
	protected void update(UpdateFixedAssetCategory record, IUserSessionContext ctx) throws DispatchException, SQLException {
		if (record.getMinDepreciationPeriod() == null && record.getMaxDepreciationPeriod() != null) {
			final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT min_depreciation_period FROM fa_asset_category WHERE id=?;", record.getId());
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next())
						throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
					if (rs.getInt(1) > record.getMaxDepreciationPeriod())
						throw new ValidationException(IFixedAssetCategory.MAX_DEPRECIATION_PERIOD, ServerErrors.INVALID_DEPRECIATION_PERIOD);
				} finally {
					Database.close(rs);
				}
			} finally {
				Database.close(stmt);
			}
		} else if (record.getMinDepreciationPeriod() != null && record.getMaxDepreciationPeriod() == null) {
			final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT max_depreciation_period FROM fa_asset_category WHERE id=?;", record.getId());
			try {
				final ResultSet rs = stmt.executeQuery();
				try {
					if (!rs.next())
						throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
					if (rs.getInt(1) < record.getMinDepreciationPeriod())
						throw new ValidationException(IFixedAssetCategory.MIN_DEPRECIATION_PERIOD, ServerErrors.INVALID_DEPRECIATION_PERIOD);
				} finally {
					Database.close(rs);
				}
			} finally {
				Database.close(stmt);
			}
		}
		sql.execute(ctx.getConnection(), record);
	}

}
