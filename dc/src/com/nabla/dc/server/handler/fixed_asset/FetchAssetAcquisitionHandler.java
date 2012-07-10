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

import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.command.FetchAssetAcquisition;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.server.model.FetchRecordRequest;

/**
 * @author nabla
 *
 */
public class FetchAssetAcquisitionHandler extends AbstractOperationHandler<FetchAssetAcquisition, FetchRecordRequest> {

	public FetchAssetAcquisitionHandler() {
		super(false, IPrivileges.ASSET_VIEW);
	}

	@Override
	protected String execute(final FetchRecordRequest request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return request.serialize(ctx.getReadConnection(),
"SELECT id, pi, acquisition_type, acquisition_date, cost" +
" FROM full_asset" +
" WHERE id=? {AND WHERE} {ORDER BY}", request.recordId);
	}

}
