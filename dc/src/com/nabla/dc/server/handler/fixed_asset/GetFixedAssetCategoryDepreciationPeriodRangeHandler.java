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

import com.nabla.dc.shared.command.fixed_asset.GetFixedAssetCategoryDepreciationPeriodRange;
import com.nabla.dc.shared.model.fixed_asset.DepreciationPeriodRange;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;


public class GetFixedAssetCategoryDepreciationPeriodRangeHandler extends AbstractHandler<GetFixedAssetCategoryDepreciationPeriodRange, DepreciationPeriodRange> {

	@Override
	public DepreciationPeriodRange execute(GetFixedAssetCategoryDepreciationPeriodRange cmd, IUserSessionContext ctx) throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT c.min_depreciation_period, c.max_depreciation_period" +
" FROM fa_asset_category AS c INNER JOIN fa_company_asset_category AS t ON t.fa_asset_category_id=c.id" +
" WHERE t.id=?;", cmd.getId());
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next())
					throw new InternalErrorException("failed to get depreciation period range for asset category");
				return new DepreciationPeriodRange(rs.getInt("min_depreciation_period"), rs.getInt("max_depreciation_period"));
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

}
