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
package com.nabla.dc.client.ui;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

/**
 * @author nabla
 *
 */
@DefaultLocale("en")
public interface IMessageResource extends Messages {

	@DefaultMessage("Logged in as {0}")
	String userloggedInAs(String userName);

	@DefaultMessage("Are you sure you want to delete these {0} users?")
	@AlternateMessage({"one", "Are you sure you want to delete user ''{1}''?"})
	String confirmRemoveUsers(@PluralCount @Optional int count, @Optional String userName);

	@DefaultMessage("Are you sure you want to delete these {0} roles?")
	@AlternateMessage({"one", "Are you sure you want to delete role ''{1}''?"})
	String confirmRemoveRoles(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("Are you sure you want to delete these {0} reports?")
	@AlternateMessage({"one", "Are you sure you want to delete report ''{1}''?"})
	String confirmRemoveReports(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("Are you sure you want to delete these {0} companies?")
	@AlternateMessage({"one", "Are you sure you want to delete company ''{1}''?"})
	String confirmRemoveCompanies(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("Are you sure you want to delete these {0} tax codes?")
	@AlternateMessage({"one", "Are you sure you want to delete tax code ''{1}''?"})
	String confirmRemoveTaxRates(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("Are you sure you want to delete these {0} accounts?")
	@AlternateMessage({"one", "Are you sure you want to delete account ''{1}''?"})
	String confirmRemoveAccounts(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("Are you sure you want to delete these {0} asset categories?")
	@AlternateMessage({"one", "Are you sure you want to delete asset category ''{1}''?"})
	String confirmRemoveFixedAssetCategories(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("Are you sure you want to delete these {0} BalanceSheet categories?")
	@AlternateMessage({"one", "Are you sure you want to delete BalanceSheet category ''{1}''?"})
	String confirmRemoveBalanceSheetCategories(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("{0} - Accounts")
	@AlternateMessage({"", "Accounts"})
	String accountListTitle(@Select String companyName);

	@DefaultMessage("{0} - Period Ends")
	@AlternateMessage({"", "Period Ends"})
	String periodEndListTitle(@Select String companyName);

	@DefaultMessage("{0} - Users")
	@AlternateMessage({"", "Users"})
	String userListTitle(@Select String companyName);

	@DefaultMessage("{0} - Companies")
	String userCompanyListTitle(String userName);

	@DefaultMessage("Are you sure you want to delete these {0} assets?")
	@AlternateMessage({"one", "Are you sure you want to delete asset ''{1}''?"})
	String confirmRemoveAssets(@PluralCount @Optional int count, @Optional String name);

	@DefaultMessage("<p>This wizard will help you split asset ''{0}'' into two assets. Overwhole no change will occur with the total cost and monthly depreciation remaining the same.<br/>Click 'Next' to continue or 'Cancel' to exit this wizard.</p>")
	String splitAssetWelcomeMessage(String assetName);

	@DefaultMessage("{0} - Transactions")
	String assetTransactionListTitle(String assetName);
}
