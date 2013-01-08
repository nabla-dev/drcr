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


public interface ITransaction extends ITransactionTable {
	static final String					PERIOD = "period";

	static final IntegerRangeConstraint	DEPRECIATION_PERIOD_CONSTRAINT = new IntegerRangeConstraint(1, 12 * 100, true);

	static final String					REPORT = "FA_TRANSACTION_LIST";

	static final String					PREFERENCE_GROUP = "fa_transaction";

}
