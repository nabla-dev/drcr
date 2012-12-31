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
package com.nabla.dc.shared;

import com.nabla.wapp.shared.auth.IRolePrivileges;


/**
 * @author nabla
 *
 */
public interface IPrivileges extends IRolePrivileges {

	static final String EC_TERMS_VIEW = "ec_terms_view";
	static final String EC_TERMS_ADD = "ec_terms_add";
	static final String EC_TERMS_REMOVE = "ec_terms_remove";
	static final String EC_TERMS_EDIT = "ec_terms_edit";

	static final String TAX_RATE_VIEW = "tax_rate_view";
	static final String TAX_RATE_ADD = "tax_rate_add";
	static final String TAX_RATE_REMOVE = "tax_rate_remove";
	static final String TAX_RATE_EDIT = "tax_rate_edit";

	static final String COMPANY_VIEW = "company_view";
	static final String COMPANY_ADD = "company_add";
	static final String COMPANY_REMOVE = "company_remove";
	static final String COMPANY_EDIT = "company_edit";

	static final String COMPANY_TAX_RATE_VIEW = "company_tax_rate_view";
	static final String COMPANY_TAX_RATE_EDIT = "company_tax_rate_edit";

	static final String COMPANY_USER_VIEW = "company_user_view";
	static final String COMPANY_USER_EDIT = "company_user_edit";

	static final String COMPANY_ASSET_CATEGORY_EDIT = "company_asset_category_edit";

	static final String FA_COMPANY_ASSET_VIEW = "fa_company_asset_view";
	static final String FA_COMPANY_ASSET_ADD = "fa_company_asset_add";
	static final String FA_COMPANY_ASSET_REMOVE = "fa_company_asset_remove";
	static final String FA_COMPANY_ASSET_EDIT = "fa_company_asset_edit";

	static final String ACCOUNT_VIEW = "account_view";
	static final String ACCOUNT_ADD = "account_add";
	static final String ACCOUNT_REMOVE = "account_remove";
	static final String ACCOUNT_EDIT = "account_edit";

	static final String PERIOD_END_VIEW = "period_end_view";
	static final String PERIOD_END_ADD = "period_end_add";
	static final String PERIOD_END_REMOVE = "period_end_remove";
	static final String PERIOD_END_EDIT = "period_end_edit";

	static final String FA_ASSET_CATEGORY_VIEW = "fa_asset_category_view";
	static final String FA_ASSET_CATEGORY_ADD = "fa_asset_category_add";
	static final String FA_ASSET_CATEGORY_REMOVE = "fa_asset_category_remove";
	static final String FA_ASSET_CATEGORY_EDIT = "fa_asset_category_edit";

	static final String FA_FINANCIAL_STATEMENT_CATEGORY_VIEW = "fa_financial_statement_category_view";
	static final String FA_FINANCIAL_STATEMENT_CATEGORY_ADD = "fa_financial_statement_category_add";
	static final String FA_FINANCIAL_STATEMENT_CATEGORY_REMOVE = "fa_financial_statement_category_remove";
	static final String FA_FINANCIAL_STATEMENT_CATEGORY_EDIT = "fa_financial_statement_category_edit";

	static final String FA_ASSET_VIEW = "fa_asset_view";
	static final String FA_ASSET_ADD = "fa_asset_add";
	static final String FA_ASSET_REMOVE = "fa_asset_remove";
	static final String FA_ASSET_EDIT = "fa_asset_edit";

	static final String IMPORT_SETTINGS = "import_settings";
}
