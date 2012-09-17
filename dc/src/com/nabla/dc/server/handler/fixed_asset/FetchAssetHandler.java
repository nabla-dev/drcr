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

import java.sql.SQLException;

import com.nabla.dc.shared.command.fixed_asset.FetchAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.json.SqlToJson;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;

/**
 * @author nabla
 *
 */
public class FetchAssetHandler extends AbstractFetchHandler<FetchAssetRecord> {

	private static final SqlToJson	fetcher = new SqlToJson(
"SELECT t.id, t.name, c.fa_asset_category_id AS 'asset_category_id', t.reference, t.location" +
", t.acquisition_date, t.acquisition_type, t.purchase_invoice" +
", (SELECT tt.amount FROM fa_transaction AS tt WHERE tt.fa_asset_id=t.id AND tt.class='COST' AND tt.type='OPENING') AS 'cost'" +
", init.amount AS 'initial_accum_depreciation', init.dep_period AS 'initial_depreciation_period'" +
", t.depreciation_period" +
", (SELECT SUM(t.amount) FROM transaction AS tt WHERE tt.fa_asset_id=a.id) AS 'residual_value'" +
" FROM fa_asset AS t INNER JOIN fa_company_asset_category AS c ON t.fa_company_asset_category_id=c.id" +
" LEFT JOIN transaction AS init ON a.id=init.fa_asset_id AND init.class='DEP' AND init.type='OPENING'" +
" WHERE t.id=?"
	);

	@Override
	public FetchResult execute(final FetchAssetRecord cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		Integer year = null;
		Integer month = null;
		PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT name, state FROM user_preference WHERE role_id=? AND category=?;", ctx.getUserId(), IAsset.PREFERENCE_GROUP);
		try {
			final ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("name").equalsIgnoreCase(IAsset.OPENING_YEAR)) {
					year = Integer.valueOf(rs.getString("state"));
				} else if (rs.getString("name").equalsIgnoreCase(IAsset.OPENING_MONTH)) {
					month = Integer.valueOf(rs.getString("state"));
				}
			}
		} catch (NumberFormatException __) {

		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
		if (year == null)
			year = Calendar.getInstance().get(Calendar.YEAR);
		if (month == null) {
			stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT first_month FROM asset INNER JOIN register ON asset.register_id=register.id WHERE asset.id=?;", request.recordId);
			try {
				final ResultSet rs = stmt.executeQuery();
				if (rs.next())
					month = rs.getInt(1);
				else
					month = 0;
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		}
		return request.serialize(ctx.getReadConnection(),
"SELECT a.id, name, asset_category_id, reference, location" +
", a.acquisition_date, a.acquisition_type, a.cost, pi, tt.amount AS 'initial_accum_dep', tt.dep_period AS 'initial_dep_period'" +
", a.dep_period, (SELECT SUM(r.amount) FROM transaction AS r WHERE r.asset_id=a.id) AS 'residual_value'" +
", ? AS 'opening_year', ? AS 'opening_month'" +
" FROM full_asset AS a" +
" LEFT JOIN transaction AS tt ON a.id=tt.asset_id AND tt.class='DEP' AND tt.type='OPENING'" +
" WHERE a.id=?", year, month, request.recordId);

		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getId());
	}
}
