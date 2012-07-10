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

import com.nabla.wapp.shared.dispatch.DispatchException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.AssetCategoryTypes;
import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.ServerErrors;
import com.nabla.fixed_assets.shared.command.AddAssetCategory;
import com.nabla.fixed_assets.shared.model.IAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.InsertStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddAssetCategoryHandler extends AbstractOperationHandler<AddAssetCategory, AddAssetCategoryHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="asset_category")
	public static class Record {

		@Element
		@IRecordField(unique=true)
		String				name;
		@IRecordField
		String				uname;
		@Element(required=false)
		@IRecordField
		Boolean				active;
		@Element
		@IRecordField
		AssetCategoryTypes	type;
		@Element
		@IRecordField
		Integer				min_dep_period;
		@Element(required=false)
		@IRecordField
		Integer				max_dep_period;

		@Validate
		public void validate() throws ValidationException {
			IAssetCategory.NAME_CONSTRAINT.validate(IAssetCategory.NAME, name);
			uname = name.toUpperCase();
			IAssetCategory.DEP_PERIOD_CONSTRAINT.validate(IAssetCategory.MIN_DEP_PERIOD, min_dep_period, ServerErrors.INVALID_DEP_PERIOD);
			if (max_dep_period == null)
				max_dep_period = min_dep_period;
			else {
				IAssetCategory.DEP_PERIOD_CONSTRAINT.validate(IAssetCategory.MAX_DEP_PERIOD, max_dep_period, ServerErrors.INVALID_DEP_PERIOD);
				if (max_dep_period < min_dep_period)
					throw new ValidationException(IAssetCategory.MAX_DEP_PERIOD, ServerErrors.INVALID_MAX_DEP_PERIOD);
			}
			if (active == null)
				active = false;
			if (type == null)
				type = AssetCategoryTypes.TANGIBLE;
		}

	}

	public static final InsertStatement<Record>	sql = new InsertStatement<Record>(Record.class);

	public AddAssetCategoryHandler() {
		super(true, IPrivileges.ASSET_CATEGORY_ADD);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			int id = sql.execute(ctx.getWriteConnection(), request);
			UserPreference.save(ctx, IAssetCategory.PREFERENCE_GROUP, "type", request.type);
			UserPreference.save(ctx, IAssetCategory.PREFERENCE_GROUP, "min_dep_period", request.min_dep_period);
			UserPreference.save(ctx, IAssetCategory.PREFERENCE_GROUP, "max_dep_period", request.max_dep_period);
			UserPreference.save(ctx, IAssetCategory.PREFERENCE_GROUP, "active", request.active);
			guard.setSuccess();
			return serialize(id);
		} finally {
			guard.close();
		}
	}

}
