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


/**
 * @author nabla
 *
 */
public interface ITransactionTable {
	static final String	TABLE = "fa_transaction";

	static final String	ID = "id";
	static final String	ASSET_ID = "fa_asset_id";
	static final String	PERIOD_END_ID = "period_end_id";
	static final String	AMOUNT = "amount";
	static final String	DEPRECIATION_PERIOD = "depreciation_period";
	static final String	CLASS = "class";
	static final String	TYPE = "type";

}
