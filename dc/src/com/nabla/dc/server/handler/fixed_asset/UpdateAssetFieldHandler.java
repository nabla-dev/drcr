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
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nabla.wapp.shared.dispatch.DispatchException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.ServerErrors;
import com.nabla.fixed_assets.shared.command.UpdateAssetField;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.SimpleString;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateAssetFieldHandler extends AbstractOperationHandler<UpdateAssetField, UpdateAssetFieldHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="asset")
	static class Record {

		@Element
		@IRecordField(id=true)
		Integer				id;
		@Element(required=false)
		@IRecordField
		SimpleString		name;
		@Element(required=false)
		@IRecordField
		SimpleString		reference;
		@Element(required=false)
		@IRecordField
		SimpleString		location;
		@Element(required=false)
		@IRecordField
		SimpleString		pi;
		@Element(required=false)
		@IRecordField
		Integer				asset_category_id;
		@Element(required=false)
		@IRecordField
		Integer				dep_period;

		@Validate
		public void validate() throws DispatchException {
			IAsset.NAME_CONSTRAINT.validate(IAsset.NAME, name);
			IAsset.REFERENCE_CONSTRAINT.validate(IAsset.REFERENCE, reference);
			IAsset.LOCATION_CONSTRAINT.validate(IAsset.LOCATION, location);
			IAsset.PI_CONSTRAINT.validate(IAsset.PI, pi);
			if (dep_period != null)
				IAsset.DEP_PERIOD_CONSTRAINT.validate(IAsset.DEP_PERIOD, dep_period);
		}

		public void validateEx(final Connection conn) throws DispatchException, SQLException {
			if (dep_period != null) {
				PreparedStatement stmt;
				if (asset_category_id != null)
					stmt = StatementFormat.prepare(conn,
"SELECT min_dep_period, max_dep_period FROM asset_category WHERE id=?;", asset_category_id);
				else
					stmt = StatementFormat.prepare(conn,
"SELECT min_dep_period, max_dep_period FROM asset INNER JOIN asset_category ON asset.asset_category_id=asset_category.id WHERE asset.id=?;", id);
				try {
					final ResultSet rs = stmt.executeQuery();
					if (!rs.next())
						throw new ValidationException(IAsset.CATEGORY, ServerErrors.UNDEFINED_CATEGORY_FOR_ASSET_REGISTER);
					if (rs.getInt("min_dep_period") > dep_period || rs.getInt("max_dep_period") < dep_period)
						throw new ValidationException(IAsset.DEP_PERIOD, CommonServerErrors.INVALID_VALUE);
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
			}
		}
	}

	public static final UpdateStatement<Record>	sql = new UpdateStatement<Record>(Record.class);

	public UpdateAssetFieldHandler() {
		super(true, IPrivileges.ASSET_EDIT);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		request.validateEx(ctx.getReadConnection());
		sql.execute(ctx.getWriteConnection(), request);
		return null;
	}

}
