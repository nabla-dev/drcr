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

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.FetchAssetListRecord;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.model.XmlResponseWriter;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class FetchAssetListRecordHandler extends AbstractHandler<FetchAssetListRecord, StringResult> {

	public FetchAssetListRecordHandler() {
		super(false, IPrivileges.ASSET_VIEW);
	}

	@Override
	public StringResult execute(final FetchAssetListRecord cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return new StringResult(serialize(ctx.getReadConnection(),
"SELECT id, name, asset_category_id, reference, location, pi, acquisition_date, acquisition_type, cost, dep_period, disposal_date, proceeds" +
" FROM full_asset" +
" WHERE id=?", cmd.getRecordId()));
	}

	private String serialize(final Connection conn, final String sql, Object... parameters) throws SQLException {
		final XmlResponseWriter response = new XmlResponseWriter();
		response.beginData();
		final PreparedStatement stmt = StatementFormat.prepare(conn, sql, parameters);
		try {
			response.write(stmt.executeQuery());
		} finally {
			try { stmt.close(); } catch (final SQLException e) {}
		}
		response.endData();
		return response.toString();
	}
}
