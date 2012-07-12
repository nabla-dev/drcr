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

import com.nabla.dc.shared.command.fixed_asset.UpdateBalanceSheetCategoryDefinition;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class UpdateBalanceSheetCategoryDefinitionHandler extends AbstractHandler<UpdateBalanceSheetCategoryDefinition, StringResult> {

	public UpdateBalanceSheetCategoryDefinitionHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final UpdateBalanceSheetCategoryDefinition record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		Database.executeUpdate(ctx.getWriteConnection(),
				record.getActive() ?
"INSERT IGNORE INTO fa_bs_category_definition (fa_bs_category_id,fa_asset_category_id) VALUES(?,?);"
						:
"DELETE FROM fa_bs_category_definition WHERE fa_bs_category_id=? AND fa_asset_category_id=?;"
				,
				record.getBalanceSheetCategoryId(), record.getAssetCategoryId());
		return null;
	}

}
