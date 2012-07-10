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

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.FetchAssetList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.server.model.FetchRequest;

/**
 * @author nabla
 *
 */
public class FetchAssetListHandler extends AbstractOperationHandler<FetchAssetList, FetchAssetListHandler.Record> {

	@Root(name="data")
	static class Record extends FetchRequest {
		@Element Integer	asset_register_id;
	}

	public FetchAssetListHandler() {
		super(false, IPrivileges.ASSET_VIEW);
	}

	@Override
	protected String execute(final Record request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return request.serialize(ctx.getReadConnection(),
"SELECT id, name, asset_category_id, reference, location, pi, acquisition_date, acquisition_type, cost, dep_period, disposal_date, proceeds" +
" FROM full_asset" +
" WHERE register_id=? {AND WHERE} {ORDER BY}", request.asset_register_id);
	}

}
