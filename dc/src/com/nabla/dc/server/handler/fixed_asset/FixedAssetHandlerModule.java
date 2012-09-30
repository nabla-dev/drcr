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

import com.nabla.wapp.server.dispatch.AbstractHandlerSubModule;

/**
 * @author nabla
 *
 */
public class FixedAssetHandlerModule extends AbstractHandlerSubModule {

	@Override
	protected void configure() {
		bindHandler(FetchFixedAssetCategoryListHandler.class);
		bindHandler(AddFixedAssetCategoryHandler.class);
		bindHandler(UpdateFixedAssetCategoryHandler.class);
		bindHandler(RemoveFixedAssetCategoryHandler.class);
		bindHandler(RestoreFixedAssetCategoryHandler.class);

		bindHandler(FetchFinancialStatementCategoryListHandler.class);
		bindHandler(AddFinancialStatementCategoryHandler.class);
		bindHandler(UpdateFinancialStatementCategoryHandler.class);
		bindHandler(RemoveFinancialStatementCategoryHandler.class);
		bindHandler(RestoreFinancialStatementCategoryHandler.class);

		bindHandler(FetchCompanyFixedAssetCategoryTreeHandler.class);
		bindHandler(FetchAvailableFixedAssetCategoryListHandler.class);
		bindHandler(UpdateCompanyFixedAssetCategoryHandler.class);

		bindHandler(FetchAssetListHandler.class);
		bindHandler(AddAssetHandler.class);
		bindHandler(UpdateAssetHandler.class);
		bindHandler(UpdateAssetFieldHandler.class);
		bindHandler(RemoveAssetHandler.class);
		bindHandler(FetchAssetRecordHandler.class);

		bindHandler(FetchAssetDisposalHandler.class);
		bindHandler(RevertAssetDisposalHandler.class);
		bindHandler(UpdateAssetDisposalHandler.class);

		bindHandler(FetchSplitAssetHandler.class);

		bindHandler(FetchAssetHandler.class);
		bindHandler(FetchCompanyFixedAssetCategoryListHandler.class);
		bindHandler(GetFixedAssetCategoryDepreciationPeriodRangeHandler.class);

		bindHandler(FetchAssetPropertiesHandler.class);

		bindHandler(RemoveTransactionHandler.class);
	}

}
