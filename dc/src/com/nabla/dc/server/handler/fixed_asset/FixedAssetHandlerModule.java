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

import com.nabla.dc.server.handler.company.settings.fixed_asset.FetchFixedAssetCategoryListHandler;
import com.nabla.wapp.server.dispatch.AbstractHandlerSubModule;

/**
 * @author nabla
 *
 */
public class FixedAssetHandlerModule extends AbstractHandlerSubModule {

	@Override
	protected void configure() {
		bindHandler(FetchFixedAssetCategoryListHandler.class);

/*
  bindHandler(GetAssetCategoryDepreciationPeriodRangeHandler.class);

		bindHandler(FetchAssetRegisterListHandler.class);
		bindHandler(AddAssetRegisterHandler.class);
		bindHandler(RemoveAssetRegisterHandler.class);
		bindHandler(RestoreAssetRegisterHandler.class);
		bindHandler(UpdateAssetRegisterHandler.class);
		bindHandler(FetchAssetRegisterUserListHandler.class);
		bindHandler(UpdateAssetRegisterUserListHandler.class);
		bindHandler(FetchAssetRegisterAssetCategoryListHandler.class);
		bindHandler(UpdateAssetRegisterAssetCategoryListHandler.class);

		bindHandler(FetchAssetRegisterBalanceSheetCategoryListHandler.class);
		bindHandler(UpdateAssetRegisterBalanceSheetCategoryListHandler.class);

		bindHandler(FetchAssetRegisterActiveAssetCategoryListHandler.class);
		bindHandler(FetchAssetListHandler.class);
		bindHandler(AddAssetHandler.class);
		bindHandler(UpdateAssetFieldHandler.class);
		bindHandler(UpdateAssetHandler.class);

		bindHandler(FetchAssetListRecordHandler.class);

		bindHandler(FetchAssetDisposalHandler.class);
		bindHandler(UpdateAssetDisposalHandler.class);
		bindHandler(RevertAssetDisposalHandler.class);

		bindHandler(FetchAssetRecordHandler.class);

		bindHandler(FetchAssetAcquisitionHandler.class);
		bindHandler(FetchAssetTransferHandler.class);

		bindHandler(SplitAssetHandler.class);

		bindHandler(ImportAssetListHandler.class);
		bindHandler(ImportSettingsHandler.class);
		bindHandler(FetchImportErrorListHandler.class);

		bindHandler(FetchTransactionListHandler.class);
		bindHandler(AddTransactionHandler.class);
		bindHandler(UpdateTransactionHandler.class);

		bindHandler(GetNewTransactionDefaultValuesHandler.class);
		*/
	}

}
