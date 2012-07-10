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
package com.nabla.dc.shared.command.fixed_asset;

import com.nabla.fixed_assets.shared.TransactionClasses;
import com.nabla.fixed_assets.shared.TransactionTypes;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class GetNewTransactionDefaultValues implements IAction<StringResult> {

	private static final long serialVersionUID = 1L;

	private Integer				assetId;
	private TransactionClasses	trxClass;
	private TransactionTypes	trxType;

	public GetNewTransactionDefaultValues() {}

	public GetNewTransactionDefaultValues(final Integer assetId, final TransactionClasses trxClass, final TransactionTypes trxType) {
		this.assetId = assetId;
		this.trxClass = trxClass;
		this.trxType = trxType;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public TransactionClasses getTransactionClass() {
		return trxClass;
	}

	public TransactionTypes getTransactionType() {
		return trxType;
	}

}
