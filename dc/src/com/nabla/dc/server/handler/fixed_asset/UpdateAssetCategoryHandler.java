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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.AssetCategoryTypes;
import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.ServerErrors;
import com.nabla.fixed_assets.shared.command.UpdateAssetCategory;
import com.nabla.fixed_assets.shared.model.IAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.dispatch.ActionException;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.SimpleString;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateAssetCategoryHandler extends AbstractOperationHandler<UpdateAssetCategory, UpdateAssetCategoryHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="asset_category")
	static class Record {

		@Element
		@IRecordField(id=true)
		Integer		id;
		@Element(required=false)
		@IRecordField(unique=true)
		SimpleString		name;
		@IRecordField
		String				uname;
		@Element(required=false)
		@IRecordField
		Boolean				active;
		@Element(required=false)
		@IRecordField
		AssetCategoryTypes	type;
		@Element(required=false)
		@IRecordField
		Integer				min_dep_period;
		@Element(required=false)
		@IRecordField
		Integer				max_dep_period;

		@Validate
		public void validate() throws ValidationException {
			IAssetCategory.NAME_CONSTRAINT.validate(IAssetCategory.NAME, name);
			if (name != null)
				uname = name.getValue().toUpperCase();
			if (min_dep_period != null)
				IAssetCategory.DEP_PERIOD_CONSTRAINT.validate(IAssetCategory.MIN_DEP_PERIOD, min_dep_period, ServerErrors.INVALID_DEP_PERIOD);
			if (max_dep_period != null)
				IAssetCategory.DEP_PERIOD_CONSTRAINT.validate(IAssetCategory.MAX_DEP_PERIOD, max_dep_period, ServerErrors.INVALID_DEP_PERIOD);
			if (max_dep_period != null && min_dep_period != null && min_dep_period > max_dep_period)
				throw new ValidationException(IAssetCategory.MAX_DEP_PERIOD, ServerErrors.INVALID_DEP_PERIOD);
		}

	}

	private static final UpdateStatement<Record>	sql = new UpdateStatement<Record>(Record.class);

	public UpdateAssetCategoryHandler() {
		super(true, IPrivileges.ASSET_CATEGORY_EDIT);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (request.min_dep_period == null && request.max_dep_period != null) {
			final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT min_dep_period FROM asset_category WHERE id=?;", request.id);
			try {
				final ResultSet rs = stmt.executeQuery();
				if (!rs.next())
					throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
				if (rs.getInt(1) > request.max_dep_period)
					throw new ValidationException(IAssetCategory.MAX_DEP_PERIOD, ServerErrors.INVALID_DEP_PERIOD);
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		} else if (request.min_dep_period != null && request.max_dep_period == null) {
			final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT max_dep_period FROM asset_category WHERE id=?;", request.id);
			try {
				final ResultSet rs = stmt.executeQuery();
				if (!rs.next())
					throw new ActionException(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
				if (rs.getInt(1) < request.min_dep_period)
					throw new ValidationException(IAssetCategory.MIN_DEP_PERIOD, ServerErrors.INVALID_DEP_PERIOD);
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		}

		sql.execute(ctx.getWriteConnection(), request);
		return null;
	}

}
