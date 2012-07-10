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
import java.sql.SQLException;

import com.nabla.wapp.shared.dispatch.DispatchException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.fixed_assets.server.AssetDepreciation;
import com.nabla.fixed_assets.server.AssetRecord;
import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.AddAsset;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.InsertStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;

/**
 * @author nabla
 *
 */
public class AddAssetHandler extends AbstractOperationHandler<AddAsset, AddAssetHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="asset")
	static class Record extends AssetRecord {
		@Element
		@IRecordField(name="register_id")
		Integer		asset_register_id;
	}

	public static final InsertStatement<Record>	sql = new InsertStatement<Record>(Record.class);

	public AddAssetHandler() {
		super(true, IPrivileges.ASSET_ADD);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// final validation
		final AssetDepreciation depreciation = new AssetDepreciation(request);
		depreciation.validateDepreciationPeriod(ctx.getReadConnection());
		if (request.createTransactions)
			depreciation.validateFinancialData();
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			int assetId = sql.execute(conn, request);
			UserPreference.save(ctx, IAsset.PREFERENCE_GROUP, "asset_category_id", request.asset_category_id);
			UserPreference.save(ctx, IAsset.PREFERENCE_GROUP, "location", request.location);
			UserPreference.save(ctx, IAsset.PREFERENCE_GROUP, "residual_value", request.residual_value);
			if (request.opening) {
				UserPreference.save(ctx, IAsset.PREFERENCE_GROUP, "opening_year", request.opening_year);
				UserPreference.save(ctx, IAsset.PREFERENCE_GROUP, "opening_month", request.opening_month);
			}
			if (request.createTransactions)
				depreciation.createTransactions(assetId, conn);
			guard.setSuccess();
			return serialize(assetId);
		} finally {
			guard.close();
		}
	}

}
