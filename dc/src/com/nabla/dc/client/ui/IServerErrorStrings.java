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

import com.nabla.wapp.shared.general.ICommonServerErrorStrings;


/**
 * @author nabla
 *
 */
public interface IServerErrorStrings extends ICommonServerErrorStrings {

	@DefaultStringValue("*** too many errors ***")
	String TOO_MANY_ERRORS();

	@DefaultStringValue("invalid tax code rate")
	String INVALID_TAX_CODE_RATE();

	@DefaultStringValue("Depreciation period must be between 12 and 1200 months")
	String INVALID_DEPRECIATION_PERIOD();

	@DefaultStringValue("Minimum depreciation period must be lower than the maximum")
	String INVALID_MIN_DEPRECIATION_PERIOD();

	@DefaultStringValue("Maximum depreciation period must be higher than the minimum")
	String INVALID_MAX_DEPRECIATION_PERIOD();

	@DefaultStringValue("Cannot be greater than the asset cost less residual value")
	String INVALID_ACCUMULATED_DEPRECIATION();

	@DefaultStringValue("Cannot be before acquisition date")
	String MUST_BE_AFTER_ACQUISITION_DATE();

	@DefaultStringValue("Cannot be before opening date")
	String MUST_BE_AFTER_OPENING_DATE();

	@DefaultStringValue("Cannot be less than the initial depreciation period")
	String DEPRECIATION_PERIOD_LESS_THAN_INITIAL();

	@DefaultStringValue("Must be equal to depreciation period")
	String INITIAL_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD();

	@DefaultStringValue("Cannot be greater or equal than the asset cost")
	String INVALID_RESIDUAL_VALUE();

	@DefaultStringValue("Must be less than depreciation period")
	String INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD();

	@DefaultStringValue("Must be greater than initial accumulated")
	String OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION();

	@DefaultStringValue("Must be less or equal to depreciation period")
	String OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD();

	@DefaultStringValue("Undefined asset category for current company")
	String UNDEFINED_ASSET_CATEGORY_FOR_COMPANY();

	@DefaultStringValue("Cannot edit asset which has been disposed of")
	String CANNOT_EDIT_DISPOSED_ASSET();

	@DefaultStringValue("There can be only one acquisition transaction")
	String ONLY_ONE_OPENING_FA_TRANSACTION();

	@DefaultStringValue("Asset is already fully depreciated")
	String FA_ASSET_FULLY_DEPRECIATED();

	@DefaultStringValue("Transaction date must be set at end of month")
	String FA_TRANSACTION_DATE_EOM();

	@DefaultStringValue("Transaction amount must be a positive value")
	String FA_TRANSACTION_AMOUNT_MUST_BE_POSITIVE();

	@DefaultStringValue("Transaction amount must be a negative value")
	String FA_TRANSACTION_AMOUNT_MUST_BE_NEGATIVE();

	@DefaultStringValue("The final NBV must be positive")
	String NBV_MUST_BE_POSITIVE();

	@DefaultStringValue("The final depreciation period cannot be more than the asset defined depreciation period")
	String INVALID_FINAL_DEPRECIATION_PERIOD();

	@DefaultStringValue("undefined asset category")
	String UNDEFINED_ASSET_CATEGORY();

	@DefaultStringValue("undefined Financial Statement category")
	String UNDEFINED_FS_CATEGORY();

}
