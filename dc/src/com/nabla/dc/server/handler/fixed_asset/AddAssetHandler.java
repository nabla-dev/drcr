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

import com.nabla.dc.shared.command.fixed_asset.AddAsset;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.InsertStatement;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddAssetHandler extends AbstractAddHandler<AddAsset> {

	public static final InsertStatement<AddAsset>	sql = new InsertStatement<AddAsset>(AddAsset.class);

	@Override
	protected int add(final AddAsset record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		// final validation
		final ValidationException errors = new ValidationException();
		Asset.validateDepreciationPeriod(ctx.getReadConnection(), record, null, errors);
		final IStraightLineDepreciation method = record.getDepreciationMethod();
		if (method != null)
			Asset.validate(method, record.getAcquisitionDate(), null, errors);
		if (!errors.isEmpty())
			throw errors;
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			int assetId = sql.execute(guard.getConnection(), record);
			final TransactionList transactions = new TransactionList(assetId);
			transactions.createTransactions(record);
			transactions.save(guard.getConnection());
			UserPreference.save(ctx, record.getCompanyId(), IAsset.PREFERENCE_GROUP, IAsset.CATEGORY, record.getCompanyAssetCategoryId());
			UserPreference.save(ctx, record.getCompanyId(), IAsset.PREFERENCE_GROUP, IAsset.LOCATION, record.getLocation());
			if (method != null)
				UserPreference.save(ctx, record.getCompanyId(), IAsset.PREFERENCE_GROUP, IAsset.RESIDUAL_VALUE, method.getResidualValue());
			guard.setSuccess();
			return assetId;
		} finally {
			guard.close();
		}
	}

}
