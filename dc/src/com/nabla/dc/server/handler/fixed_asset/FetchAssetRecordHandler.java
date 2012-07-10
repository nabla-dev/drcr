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
import java.util.Calendar;

import com.nabla.wapp.shared.dispatch.DispatchException;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.FetchAssetRecord;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.server.model.FetchRecordRequest;

/**
 * @author nabla
 *
 */
public class FetchAssetRecordHandler extends AbstractOperationHandler<FetchAssetRecord, FetchRecordRequest> {

	public FetchAssetRecordHandler() {
		super(false, IPrivileges.ASSET_EDIT, IPrivileges.ASSET_VIEW);
	}

	@Override
	protected String execute(final FetchRecordRequest request, final IUserSessionContext ctx) throws DispatchException, SQLException {
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
	}

}
