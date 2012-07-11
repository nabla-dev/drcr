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
package com.nabla.dc.server;

import com.google.inject.Singleton;
import com.nabla.dc.server.handler.FetchCompanyNameHandler;
import com.nabla.dc.server.handler.FetchImportErrorListHandler;
import com.nabla.dc.server.handler.company.settings.AddAccountHandler;
import com.nabla.dc.server.handler.company.settings.ChangeCompanyLogoHandler;
import com.nabla.dc.server.handler.company.settings.FetchAccountListHandler;
import com.nabla.dc.server.handler.company.settings.FetchCompanyTaxRateListHandler;
import com.nabla.dc.server.handler.company.settings.FetchCompanyUserListHandler;
import com.nabla.dc.server.handler.company.settings.FetchPeriodEndTreeHandler;
import com.nabla.dc.server.handler.company.settings.ImportAccountListHandler;
import com.nabla.dc.server.handler.company.settings.RemoveAccountHandler;
import com.nabla.dc.server.handler.company.settings.RestoreAccountHandler;
import com.nabla.dc.server.handler.company.settings.UpdateAccountHandler;
import com.nabla.dc.server.handler.company.settings.UpdateCompanyTaxRateHandler;
import com.nabla.dc.server.handler.company.settings.UpdateCompanyUserHandler;
import com.nabla.dc.server.handler.company.settings.UpdateFinancialYearHandler;
import com.nabla.dc.server.handler.company.settings.UpdatePeriodEndHandler;
import com.nabla.dc.server.handler.fixed_asset.AddAssetCategoryHandler;
import com.nabla.dc.server.handler.fixed_asset.FetchAssetCategoryListHandler;
import com.nabla.dc.server.handler.fixed_asset.RemoveAssetCategoryHandler;
import com.nabla.dc.server.handler.fixed_asset.RemoveAssetHandler;
import com.nabla.dc.server.handler.fixed_asset.RemoveBalanceSheetCategoryHandler;
import com.nabla.dc.server.handler.fixed_asset.RemoveTransactionHandler;
import com.nabla.dc.server.handler.fixed_asset.RestoreAssetCategoryHandler;
import com.nabla.dc.server.handler.fixed_asset.UpdateAssetCategoryHandler;
import com.nabla.dc.server.handler.settings.AddCompanyHandler;
import com.nabla.dc.server.handler.settings.AddECTermHandler;
import com.nabla.dc.server.handler.settings.AddTaxRateHandler;
import com.nabla.dc.server.handler.settings.FetchCompanyListHandler;
import com.nabla.dc.server.handler.settings.FetchECTermListHandler;
import com.nabla.dc.server.handler.settings.FetchTaxRateListHandler;
import com.nabla.dc.server.handler.settings.RemoveCompanyHandler;
import com.nabla.dc.server.handler.settings.RemoveECTermHandler;
import com.nabla.dc.server.handler.settings.RemoveTaxRateHandler;
import com.nabla.dc.server.handler.settings.RestoreCompanyHandler;
import com.nabla.dc.server.handler.settings.RestoreECTermHandler;
import com.nabla.dc.server.handler.settings.RestoreTaxRateHandler;
import com.nabla.dc.server.handler.settings.UpdateCompanyHandler;
import com.nabla.dc.server.handler.settings.UpdateECTermHandler;
import com.nabla.dc.server.handler.settings.UpdateTaxRateHandler;
import com.nabla.wapp.server.auth.IUserSessionContextProvider;
import com.nabla.wapp.server.basic.general.UserSessionContextProvider;
import com.nabla.wapp.server.basic.handler.AddRoleHandler;
import com.nabla.wapp.server.basic.handler.AddUserHandler;
import com.nabla.wapp.server.basic.handler.ChangeUserPasswordHandler;
import com.nabla.wapp.server.basic.handler.FetchRoleDefinitionHandler;
import com.nabla.wapp.server.basic.handler.FetchRoleListHandler;
import com.nabla.wapp.server.basic.handler.FetchRoleNameHandler;
import com.nabla.wapp.server.basic.handler.FetchUserDefinitionHandler;
import com.nabla.wapp.server.basic.handler.FetchUserListHandler;
import com.nabla.wapp.server.basic.handler.FetchUserNameHandler;
import com.nabla.wapp.server.basic.handler.GetFormDefaultValuesHandler;
import com.nabla.wapp.server.basic.handler.IsUserInRoleHandler;
import com.nabla.wapp.server.basic.handler.LoadListGridStateHandler;
import com.nabla.wapp.server.basic.handler.RemoveRoleHandler;
import com.nabla.wapp.server.basic.handler.RemoveUserHandler;
import com.nabla.wapp.server.basic.handler.RestoreUserHandler;
import com.nabla.wapp.server.basic.handler.SaveListGridStateHandler;
import com.nabla.wapp.server.basic.handler.UpdateRoleDefinitionHandler;
import com.nabla.wapp.server.basic.handler.UpdateRoleHandler;
import com.nabla.wapp.server.basic.handler.UpdateUserDefinitionHandler;
import com.nabla.wapp.server.basic.handler.UpdateUserHandler;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadOnlyDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.dispatch.AbstractHandlerModule;

/**
 * @author nabla
 *
 */
public class HandlerModule extends AbstractHandlerModule {

//	private static final boolean		RECOMPILE_REPORTS = false;

	@Override
	protected void configure() {
		super.configure();

//		bindConstant().annotatedWith(IReCompileReports.class).to(RECOMPILE_REPORTS);

		bind(IDatabase.class).annotatedWith(IReadWriteDatabase.class).to(MyReadWriteDatabase.class).in(Singleton.class);
		bind(IDatabase.class).annotatedWith(IReadOnlyDatabase.class).to(MyReadOnlyDatabase.class).in(Singleton.class);
		bind(IUserSessionContextProvider.class).to(UserSessionContextProvider.class).in(Singleton.class);

		bindHandler(IsUserInRoleHandler.class);

		bindHandler(LoadListGridStateHandler.class);
		bindHandler(SaveListGridStateHandler.class);
		bindHandler(GetFormDefaultValuesHandler.class);
		bindHandler(FetchImportErrorListHandler.class);

		bindHandler(FetchRoleListHandler.class);
		bindHandler(AddRoleHandler.class);
		bindHandler(UpdateRoleHandler.class);
		bindHandler(RemoveRoleHandler.class);
		bindHandler(FetchRoleNameHandler.class);
		bindHandler(FetchRoleDefinitionHandler.class);
		bindHandler(UpdateRoleDefinitionHandler.class);

		bindHandler(FetchUserListHandler.class);
		bindHandler(AddUserHandler.class);
		bindHandler(UpdateUserHandler.class);
		bindHandler(RemoveUserHandler.class);
		bindHandler(RestoreUserHandler.class);
		bindHandler(FetchUserNameHandler.class);
		bindHandler(FetchUserDefinitionHandler.class);
		bindHandler(UpdateUserDefinitionHandler.class);

		bindHandler(ChangeUserPasswordHandler.class);

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

		bindHandler(FetchCompanyListHandler.class);
		bindHandler(AddCompanyHandler.class);
		bindHandler(UpdateCompanyHandler.class);
		bindHandler(RemoveCompanyHandler.class);
		bindHandler(RestoreCompanyHandler.class);
		bindHandler(ChangeCompanyLogoHandler.class);
		bindHandler(FetchCompanyNameHandler.class);

		bindHandler(FetchCompanyTaxRateListHandler.class);
		bindHandler(UpdateCompanyTaxRateHandler.class);

		bindHandler(FetchCompanyUserListHandler.class);
		bindHandler(UpdateCompanyUserHandler.class);

		bindHandler(com.nabla.dc.server.handler.FetchUserCompanyListHandler.class);
		bindHandler(com.nabla.dc.server.handler.company.settings.FetchUserCompanyListHandler.class);

		bindHandler(FetchAccountListHandler.class);
		bindHandler(AddAccountHandler.class);
		bindHandler(UpdateAccountHandler.class);
		bindHandler(RemoveAccountHandler.class);
		bindHandler(RestoreAccountHandler.class);
		bindHandler(ImportAccountListHandler.class);

		bindHandler(FetchPeriodEndTreeHandler.class);
		bindHandler(UpdatePeriodEndHandler.class);
		bindHandler(UpdateFinancialYearHandler.class);

		bindHandler(FetchAssetCategoryListHandler.class);
		bindHandler(AddAssetCategoryHandler.class);
		bindHandler(UpdateAssetCategoryHandler.class);
		bindHandler(RemoveAssetCategoryHandler.class);
		bindHandler(RestoreAssetCategoryHandler.class);

		bindHandler(RemoveBalanceSheetCategoryHandler.class);

		bindHandler(RemoveAssetHandler.class);

		bindHandler(RemoveTransactionHandler.class);
/*



  bindHandler(GetAssetCategoryDepreciationPeriodRangeHandler.class);

		bindHandler(FetchBalanceSheetCategoryListHandler.class);
		bindHandler(AddBalanceSheetCategoryHandler.class);

		bindHandler(RestoreBalanceSheetCategoryHandler.class);
		bindHandler(UpdateBalanceSheetCategoryHandler.class);

		bindHandler(FetchBalanceSheetCategoryDefinitionHandler.class);
		bindHandler(UpdateBalanceSheetCategoryDefinitionHandler.class);

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

		bindHandler(AddListGridFilterHandler.class);
		bindHandler(FetchListGridFilterListHandler.class);
		bindHandler(UpdateListGridFilterHandler.class);
		bindHandler(RemoveListGridFilterHandler.class);

		bindHandler(LoadUserPreferenceHandler.class);
		bindHandler(SaveUserPreferenceHandler.class);
		bindHandler(GetFormDefaultValuesHandler.class);

		bindHandler(FetchReportListHandler.class);
		bindHandler(FetchUserReportListHandler.class);
		bindHandler(FetchReportPermissionsComboBoxHandler.class);
		bindHandler(RemoveReportHandler.class);
		bindHandler(AddReportHandler.class);
		bindHandler(UpdateReportHandler.class);

		bind(String.class).annotatedWith(IReportFolder.class).toProvider(ReportFolderProvider.class).in(Singleton.class);
		bind(IReportParameterTypeValidator.class).to(ReportParameterTypeValidator.class).in(Singleton.class);

		bindHandler(GetBuiltInReportHandler.class);
		bindHandler(GetSimpleReportHandler.class);
		bindHandler(GetReportHandler.class);
		bindHandler(GetReportParameterDefaultValuesHandler.class);
		bindHandler(FetchReportParameterAccountListHandler.class);
		*/
	}

}
