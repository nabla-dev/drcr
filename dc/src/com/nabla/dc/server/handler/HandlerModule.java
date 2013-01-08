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
package com.nabla.dc.server.handler;

import com.google.inject.Singleton;
import com.nabla.dc.server.MyReadOnlyDatabase;
import com.nabla.dc.server.MyReadWriteDatabase;
import com.nabla.dc.server.handler.company.CompanyHandlerModule;
import com.nabla.dc.server.handler.fixed_asset.FixedAssetHandlerModule;
import com.nabla.dc.server.handler.general.GeneralHandlerModule;
import com.nabla.dc.server.handler.options.OptionsHandlerModule;
import com.nabla.dc.server.report.ReportCategoryValidator;
import com.nabla.wapp.report.server.IReportCategoryValidator;
import com.nabla.wapp.server.auth.IUserSessionContextProvider;
import com.nabla.wapp.server.basic.general.UserSessionContextProvider;
import com.nabla.wapp.server.basic.handler.GetFormDefaultValuesHandler;
import com.nabla.wapp.server.basic.handler.IsUserInRoleHandler;
import com.nabla.wapp.server.basic.handler.LoadListGridStateHandler;
import com.nabla.wapp.server.basic.handler.SaveListGridStateHandler;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadOnlyDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.dispatch.AbstractHandlerModule;


public class HandlerModule extends AbstractHandlerModule {

	@Override
	protected void configure() {
		super.configure();
		install(new OptionsHandlerModule());
		install(new GeneralHandlerModule());
		install(new CompanyHandlerModule());
		install(new FixedAssetHandlerModule());
		install(new com.nabla.wapp.report.server.handler.HandlerModule());

		bind(IDatabase.class).annotatedWith(IReadWriteDatabase.class).to(MyReadWriteDatabase.class).in(Singleton.class);
		bind(IDatabase.class).annotatedWith(IReadOnlyDatabase.class).to(MyReadOnlyDatabase.class).in(Singleton.class);
		bind(IUserSessionContextProvider.class).to(UserSessionContextProvider.class).in(Singleton.class);

		bindHandler(IsUserInRoleHandler.class);

		bindHandler(LoadListGridStateHandler.class);
		bindHandler(SaveListGridStateHandler.class);
		bindHandler(GetFormDefaultValuesHandler.class);
		bindHandler(FetchImportErrorListHandler.class);

		bindHandler(FetchUserCompanyListHandler.class);

		bindHandler(ImportSettingsHandler.class);
		bindHandler(ExportSettingsHandler.class);

		bindHandler(ImportAssetsHandler.class);

		bind(IReportCategoryValidator.class).to(ReportCategoryValidator.class).in(Singleton.class);
/*
		bindHandler(AddListGridFilterHandler.class);
		bindHandler(FetchListGridFilterListHandler.class);
		bindHandler(UpdateListGridFilterHandler.class);
		bindHandler(RemoveListGridFilterHandler.class);

bind(IReportParameterTypeValidator.class).to(ReportParameterTypeValidator.class).in(Singleton.class);
		*/
	}

}
