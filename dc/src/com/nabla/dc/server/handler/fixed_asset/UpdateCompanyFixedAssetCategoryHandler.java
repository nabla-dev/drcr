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

import com.nabla.dc.shared.command.fixed_asset.UpdateCompanyFixedAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class UpdateCompanyFixedAssetCategoryHandler extends AbstractHandler<UpdateCompanyFixedAssetCategory, StringResult> {

//	private final SqlInsert<UpdateCompanyFixedAssetCategory> sql = new SqlInsert<UpdateCompanyFixedAssetCategory>(UpdateCompanyFixedAssetCategory.class, SqlInsertOptions.OVERWRITE);


	@Override
	public StringResult execute(final UpdateCompanyFixedAssetCategory cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
/*		final ImportErrorManager errors = new ImportErrorManager(writeDb, cmd.getBatchId());
		try {
			final JsonResponse json = new JsonResponse();
			json.put(IImportAccount.SUCCESS, add(cmd, errors, ctx));
			return json.toStringResult();
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("failed to parse accounts from CSV data", e);
			Util.throwInternalErrorException(e);
		} finally {
			errors.close();
		}*/
		return null;
	}

}
