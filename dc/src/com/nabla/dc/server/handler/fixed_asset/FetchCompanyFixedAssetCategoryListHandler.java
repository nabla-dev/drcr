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

import com.nabla.dc.shared.command.fixed_asset.FetchCompanyFixedAssetCategoryList;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIntToJson;
import com.nabla.wapp.server.json.OdbcStringToJson;
import com.nabla.wapp.server.json.SimpleJsonFetch;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;
import com.nabla.wapp.shared.model.IFieldReservedNames;

/**
 * @author nabla
 *
 */
public class FetchCompanyFixedAssetCategoryListHandler extends AbstractFetchHandler<FetchCompanyFixedAssetCategoryList> {
	private static final SimpleJsonFetch	fetcher = new SimpleJsonFetch(
"SELECT isFolder, parentId, id, name, active, iid FROM" +
"(SELECT TRUE AS 'isFolder', NULL AS 'parentId', CONCAT('f',t.id) AS 'id', t.name, NULL AS 'active', NULL AS 'iid'" +
" FROM fa_fs_category AS t" +
" WHERE t.active=TRUE AND t.uname IS NOT NULL" +
" UNION" +
" SELECT FALSE AS 'isFolder', CONCAT('f',r.fa_fs_category_id) AS 'parentId', CONCAT('a',t.id) AS 'id', t.name, r.active, t.id AS 'iid'" +
" FROM fa_asset_category AS t INNER JOIN fa_company_asset_category AS r ON t.id=r.fa_asset_category_id AND r.company_id=?" +
" WHERE t.active=TRUE AND t.uname IS NOT NULL AND r.fa_fs_category_id IS NOT NULL" +
") dt",
			new OdbcBooleanToJson(IFieldReservedNames.TREEGRID_IS_FOLDER),
			new OdbcStringToJson(IFieldReservedNames.TREEGRID_PARENT_ID),
			new OdbcStringToJson(IdField.NAME),
			new OdbcStringToJson(IFixedAssetCategory.NAME),
			new OdbcBooleanToJson(IFixedAssetCategory.ACTIVE),
			new OdbcIntToJson("iid")
		);

	@Override
	public FetchResult execute(final FetchCompanyFixedAssetCategoryList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		return fetcher.serialize(cmd, ctx.getConnection(), cmd.getCompanyId());
/*		return (cmd.getParentId() == null) ?
			fetcher.serialize(cmd, ctx.getConnection(), SQL_FS_CATEGORY)
			:
			fetcher.serialize(cmd, ctx.getConnection(), SQL_ASSET_CATEGORY, cmd.getCompanyId(), cmd.getParentId());*/
	}

}
