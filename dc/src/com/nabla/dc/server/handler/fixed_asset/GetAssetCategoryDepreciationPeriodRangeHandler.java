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

import com.nabla.fixed_assets.shared.AssetCategoryDepreciationPeriodRange;
import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.GetAssetCategoryDepreciationPeriodRange;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
public class GetAssetCategoryDepreciationPeriodRangeHandler extends AbstractHandler<GetAssetCategoryDepreciationPeriodRange, AssetCategoryDepreciationPeriodRange> {

	public GetAssetCategoryDepreciationPeriodRangeHandler() {
		super(false, IPrivileges.ASSET_ADD, IPrivileges.ASSET_EDIT);
	}

	@Override
	public AssetCategoryDepreciationPeriodRange execute(GetAssetCategoryDepreciationPeriodRange cmd, IUserSessionContext ctx) throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT min_dep_period, max_dep_period FROM asset_category WHERE id=?;", cmd.getId());
		try {
			final ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				throw new InternalErrorException("failed to get depreciation period range for asset category");
			return new AssetCategoryDepreciationPeriodRange(rs.getInt("min_dep_period"), rs.getInt("max_dep_period"));
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
	}

}
