/**
* Copyright 2010 nabla
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
package com.nabla.dc.shared.model.fixed_assets;

import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;


/**
 * @author nabla
 *
 */
public interface IAsset {

	static final String					ASSET_REGISTER_ID = "asset_register_id";

	static final String					NAME = "name";
	static final String					NAME_A = "nameA";
	static final String					NAME_B = "nameB";
	static final TextLengthConstraint	NAME_CONSTRAINT = new TextLengthConstraint(1, 128);

	static final String					CATEGORY = "asset_category_id";

	static final String					REFERENCE = "reference";
	static final String					REFERENCE_A = "referenceA";
	static final String					REFERENCE_B = "referenceB";
	static final TextLengthConstraint	REFERENCE_CONSTRAINT = new TextLengthConstraint(0, 32);

	static final String					LOCATION = "location";
	static final TextLengthConstraint	LOCATION_CONSTRAINT = new TextLengthConstraint(0, 128);

	static final String					ACQUISITION_DATE = "acquisition_date";
	static final String					ACQUISITION_TYPE = "acquisition_type";
	static final String					COST = "cost";
	// if Transfer
	static final String					INITIAL_ACCUM_DEP = "initial_accum_dep";
	static final int					DEFAULT_INITIAL_ACCUM_DEP = 0;
	static final String					INITIAL_DEP_PERIOD = "initial_dep_period";
	static final int					DEFAULT_INITIAL_DEP_PERIOD = 0;
	static final IntegerRangeConstraint	INITIAL_DEP_PERIOD_CONSTRAINT = new IntegerRangeConstraint(0, IAssetCategory.DEP_PERIOD_CONSTRAINT.getMaxValue());
	// if import asset
	static final String					OPENING = "opening";
	static final String					OPENING_YEAR = "opening_year";
	static final IntegerRangeConstraint	OPENING_YEAR_CONSTRAINT = new IntegerRangeConstraint(1980, 2999);
	static final String					OPENING_MONTH = "opening_month";
	static final String					OPENING_ACCUM_DEP = "opening_accum_dep";
	static final String					OPENING_DEP_PERIOD = "opening_dep_period";

	static final String					PI = "pi";
	static final TextLengthConstraint	PI_CONSTRAINT = new TextLengthConstraint(0, 64);

	static final String					DEP_PERIOD = "dep_period";
	static final IntegerRangeConstraint	DEP_PERIOD_CONSTRAINT = IAssetCategory.DEP_PERIOD_CONSTRAINT;

	static final String					RESIDUAL_VALUE = "residual_value";
	static final int					DEFAULT_RESIDUAL_VALUE = 1;

	static final String					DISPOSAL_DATE = "disposal_date";
	static final String					DISPOSAL_TYPE = "disposal_type";
	static final String					PROCEEDS = "proceeds";

	static final String					CREATE_TRANSACTIONS = "createTransactions";

	static final String					REPORT = "ASSET_LIST";

	static final String					PREFERENCE_GROUP = "asset";
	static final String					IMPORT_PREFERENCE_GROUP = "import_asset";
	static final String					DISPOSAL_PREFERENCE_GROUP = "asset_disposal";
}
