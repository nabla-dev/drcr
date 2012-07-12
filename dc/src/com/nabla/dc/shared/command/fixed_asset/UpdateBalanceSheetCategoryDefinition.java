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

import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class UpdateBalanceSheetCategoryDefinition implements IAction<StringResult> {

	private Integer		balanceSheetCategoryId;
	private Integer		assetCategoryId;
	private Boolean		active;

	protected UpdateBalanceSheetCategoryDefinition() {}	// for serialization only

	public UpdateBalanceSheetCategoryDefinition(final Integer balanceSheetCategoryId, final Integer assetCategoryId, final Boolean active) {
		this.balanceSheetCategoryId = balanceSheetCategoryId;
		this.assetCategoryId = assetCategoryId;
		this.active = active;
	}

	public Integer getBalanceSheetCategoryId() {
		return balanceSheetCategoryId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public Boolean getActive() {
		return active;
	}

}
