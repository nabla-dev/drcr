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
import com.nabla.fixed_assets.shared.command.FetchAssetDisposal;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.server.model.FetchRecordRequest;

/**
 * @author nabla
 *
 */
public class FetchAssetDisposalHandler extends AbstractOperationHandler<FetchAssetDisposal, FetchRecordRequest> {

	public FetchAssetDisposalHandler() {
		super(false, IPrivileges.ASSET_EDIT, IPrivileges.ASSET_VIEW);
	}

	@Override
	protected String execute(final FetchRecordRequest request, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return request.serialize(ctx.getReadConnection(),
"SELECT * FROM (" +
"SELECT a.id, IFNULL(a.disposal_date, (SELECT p.state FROM user_preference AS p WHERE p.role_id=? AND p.category=? AND p.name='disposal_date')) AS 'disposal_date', IFNULL(disposal_type, (SELECT p.state FROM user_preference AS p WHERE p.role_id=? AND p.category=? AND p.name='disposal_type')) AS 'disposal_type', proceeds" +
" FROM full_asset AS a" +
" WHERE a.id=?" +
") AS dt {WHERE} {ORDER BY}",
ctx.getUserId(), IAsset.DISPOSAL_PREFERENCE_GROUP, ctx.getUserId(), IAsset.DISPOSAL_PREFERENCE_GROUP, request.recordId);
	}

}
