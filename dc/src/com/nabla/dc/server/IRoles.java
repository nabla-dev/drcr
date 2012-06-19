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

import com.nabla.dc.shared.IPrivileges;
import com.nabla.wapp.server.auth.IRole;

/**
 * @author nabla
 *
 */
public interface IRoles extends IPrivileges {

	@IRole({USER_ADD, USER_REMOVE, USER_EDIT, USER_VIEW})
	static String USER_RW = "user_rw";
	@IRole(USER_VIEW)
	static String USER_RO = "user_ro";
	@IRole({ROLE_ADD, ROLE_REMOVE, ROLE_EDIT, ROLE_VIEW})
	static String ROLE_RW = "role_rw";
	@IRole(ROLE_VIEW)
	static String ROLE_RO = "role_ro";
	@IRole({USER_RW, ROLE_RW})
	static String USER_MANAGER = "user_manager";
/*
	@IRole({REPORT_ADD, REPORT_REMOVE, REPORT_EDIT, REPORT_VIEW})
	static String REPORT_RW = "report_rw";
	@IRole(REPORT_VIEW)
	static String REPORT_RO = "report_ro";
*/
	@IRole({COMPANY_TAX_RATE_EDIT, COMPANY_TAX_RATE_VIEW})
	static String COMPANY_TAX_RATE_RW = "company_tax_rate_rw";
	@IRole(COMPANY_TAX_RATE_VIEW)
	static String COMPANY_TAX_RATE_RO = "company_tax_rate_ro";

	@IRole({COMPANY_USER_EDIT, COMPANY_USER_VIEW})
	static String COMPANY_USER_RW = "company_user_rw";
	@IRole(COMPANY_USER_VIEW)
	static String COMPANY_USER_RO = "company_user_ro";

	@IRole({ACCOUNT_ADD, ACCOUNT_REMOVE, ACCOUNT_EDIT, ACCOUNT_VIEW})
	static String ACCOUNT_RW = "account_rw";
	@IRole(ACCOUNT_VIEW)
	static String ACCOUNT_RO = "account_ro";

	@IRole({PERIOD_END_ADD, PERIOD_END_REMOVE, PERIOD_END_EDIT, PERIOD_END_VIEW})
	static String PERIOD_END_RW = "period_end_rw";
	@IRole(PERIOD_END_VIEW)
	static String PERIOD_END_RO = "period_end_ro";

	@IRole({COMPANY_TAX_RATE_RW,COMPANY_USER_RW,ACCOUNT_RW,PERIOD_END_RW})
	static String COMPANY_SETTINGS_RW = "company_settings_rw";
	@IRole({COMPANY_USER_RO,ACCOUNT_RO,PERIOD_END_RO})
	static String COMPANY_SETTINGS_RO = "company_settings_ro";

	@IRole({COMPANY_ADD, COMPANY_REMOVE, COMPANY_EDIT, COMPANY_VIEW,COMPANY_SETTINGS_RW})
	static String COMPANY_RW = "company_rw";
	@IRole({COMPANY_VIEW, COMPANY_SETTINGS_RO})
	static String COMPANY_RO = "company_ro";

	@IRole({TAX_RATE_ADD, TAX_RATE_REMOVE, TAX_RATE_EDIT, TAX_RATE_VIEW})
	static String TAX_RATE_RW = "tax_rate_rw";
	@IRole(TAX_RATE_VIEW)
	static String TAX_RATE_RO = "tax_rate_ro";

	@IRole({USER_MANAGER/*, REPORT_RW*/})
	static String ADMINISTRATOR = "administrator";

	@IRole({USER_RO, ROLE_RO/*, REPORT_RO*/})
	static String GUEST = "guest";
}
