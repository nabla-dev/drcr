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
package com.nabla.dc.shared.model.fixed_asset;

import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;


/**
 * @author nabla
 *
 */
public interface IAsset extends IAssetTable {

	static final String					COMPANY_ID = "company_id";
	static final String					CATEGORY = "category";

	static final TextLengthConstraint		NAME_CONSTRAINT = new TextLengthConstraint(1, 128, true);
	static final TextLengthConstraint		REFERENCE_CONSTRAINT = new TextLengthConstraint(0, 32, true);
	static final TextLengthConstraint		LOCATION_CONSTRAINT = new TextLengthConstraint(0, 128, true);
	static final TextLengthConstraint		PURCHASE_INVOICE_CONSTRAINT = new TextLengthConstraint(0, 64, true);
	static final IntegerRangeConstraint	DEPRECIATION_PERIOD_CONSTRAINT = IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT;

	static final String					COST = "cost";
	// if Transfer
	static final String					INITIAL_ACCUMULATED_DEPRECIATION = "initial_accumulated_depreciation";
	static final int						DEFAULT_INITIAL_ACCUMULATED_DEPRECIATION = 0;
	static final String					INITIAL_DEPRECIATION_PERIOD = "initial_depreciation_period";
	static final int						DEFAULT_INITIAL_DEPRECIATION_PERIOD = 0;
	static final IntegerRangeConstraint	INITIAL_DEPRECIATION_PERIOD_CONSTRAINT = new IntegerRangeConstraint(0, IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT.getMaxValue(), true);
	// if import asset
	static final String					OPENING = "opening";
	static final String					OPENING_YEAR = "opening_year";
	static final IntegerRangeConstraint	OPENING_YEAR_CONSTRAINT = new IntegerRangeConstraint(1980, 2999, true);
	static final String					OPENING_MONTH = "opening_month";
	static final String					OPENING_ACCUMULATED_DEPRECIATION = "opening_accumulated_depreciation";
	static final String					OPENING_DEPRECIATION_PERIOD = "opening_depreciation_period";

	static final String					RESIDUAL_VALUE = "residual_value";
	static final int						DEFAULT_RESIDUAL_VALUE = 1;

	static final String					CREATE_TRANSACTIONS = "createTransactions";

	static final String					REPORT = "FA_ASSET_LIST";

	static final String					PREFERENCE_GROUP = "fa_asset";
	static final String					IMPORT_PREFERENCE_GROUP = "fa_import_asset";
	static final String					DISPOSAL_PREFERENCE_GROUP = "fa_asset_disposal";
}
