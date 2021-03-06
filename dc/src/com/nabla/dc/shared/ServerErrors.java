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


public enum ServerErrors {

	TOO_MANY_ERRORS,

	INVALID_REPORT_PARAMETER_MODEL,
	INVALID_TAX_CODE_RATE,

	INVALID_DEPRECIATION_PERIOD,
	INVALID_MIN_DEPRECIATION_PERIOD,
	INVALID_MAX_DEPRECIATION_PERIOD,
	INVALID_ACCUMULATED_DEPRECIATION,
	INVALID_RESIDUAL_VALUE,
	MUST_BE_AFTER_ACQUISITION_DATE,
	OPENING_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD,
	OPENING_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD,

	FA_SPLIT_ASSET_NOT_SAME_NAME,

	CANNOT_EDIT_DISPOSED_ASSET,
	ONLY_ONE_OPENING_FA_TRANSACTION,
	FA_ASSET_FULLY_DEPRECIATED,
	FA_TRANSACTION_DATE_EOM,
	FA_TRANSACTION_AMOUNT_MUST_BE_POSITIVE,
	FA_TRANSACTION_AMOUNT_MUST_BE_NEGATIVE,
	NBV_MUST_BE_POSITIVE,
	INVALID_FINAL_DEPRECIATION_PERIOD,

	UNDEFINED_ASSET_CATEGORY_FOR_COMPANY,
	UNDEFINED_ASSET_CATEGORY,
	UNDEFINED_FS_CATEGORY
}
