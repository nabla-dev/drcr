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


/**
 * @author nabla
 *
 */
public interface ITransaction {

	static final String					ASSET_ID = "asset_id";

	static final String					DATE = "date";
	static final String					CLASS = "class";
	static final String					TYPE = "type";
	static final String					AMOUNT = "amount";
	static final String					DEP_PERIOD = "dep_period";
	static final IntegerRangeConstraint	DEP_PERIOD_CONSTRAINT = new IntegerRangeConstraint(1, 12 * 100);

	static final String					REPORT = "TRANSACTION_LIST";

	static final String					PREFERENCE_GROUP = "transaction";

}
