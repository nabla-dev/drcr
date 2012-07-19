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
package com.nabla.dc.server.handler.settings;

import com.nabla.dc.server.handler.settings.company.AddCompanyHandler;
import com.nabla.dc.server.handler.settings.company.AddECTermHandler;
import com.nabla.dc.server.handler.settings.company.AddTaxRateHandler;
import com.nabla.dc.server.handler.settings.company.FetchCompanyListHandler;
import com.nabla.dc.server.handler.settings.company.FetchECTermListHandler;
import com.nabla.dc.server.handler.settings.company.FetchTaxRateListHandler;
import com.nabla.dc.server.handler.settings.company.RemoveCompanyHandler;
import com.nabla.dc.server.handler.settings.company.RemoveECTermHandler;
import com.nabla.dc.server.handler.settings.company.RemoveTaxRateHandler;
import com.nabla.dc.server.handler.settings.company.RestoreCompanyHandler;
import com.nabla.dc.server.handler.settings.company.RestoreECTermHandler;
import com.nabla.dc.server.handler.settings.company.RestoreTaxRateHandler;
import com.nabla.dc.server.handler.settings.company.UpdateCompanyHandler;
import com.nabla.dc.server.handler.settings.company.UpdateECTermHandler;
import com.nabla.dc.server.handler.settings.company.UpdateTaxRateHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.AddFinancialStatementCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.AddFixedAssetCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.FetchFinancialStatementCategoryListHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.FetchFixedAssetCategoryListHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.RemoveFinancialStatementCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.RemoveFixedAssetCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.RestoreFinancialStatementCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.RestoreFixedAssetCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.UpdateFinancialStatementCategoryHandler;
import com.nabla.dc.server.handler.settings.fixed_asset.UpdateFixedAssetCategoryHandler;
import com.nabla.wapp.server.dispatch.AbstractHandlerSubModule;

/**
 * @author nabla
 *
 */
public class SettingsHandlerModule extends AbstractHandlerSubModule {

	@Override
	protected void configure() {
		bindHandler(FetchCompanyListHandler.class);
		bindHandler(AddCompanyHandler.class);
		bindHandler(UpdateCompanyHandler.class);
		bindHandler(RemoveCompanyHandler.class);
		bindHandler(RestoreCompanyHandler.class);

		bindHandler(FetchECTermListHandler.class);
		bindHandler(AddECTermHandler.class);
		bindHandler(UpdateECTermHandler.class);
		bindHandler(RemoveECTermHandler.class);
		bindHandler(RestoreECTermHandler.class);

		bindHandler(FetchTaxRateListHandler.class);
		bindHandler(AddTaxRateHandler.class);
		bindHandler(UpdateTaxRateHandler.class);
		bindHandler(RemoveTaxRateHandler.class);
		bindHandler(RestoreTaxRateHandler.class);

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
	}

}
