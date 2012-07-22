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

import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
@IRecordTable(name="fa_company_asset_category")
public class UpdateCompanyFixedAssetCategory implements IAction<StringResult> {

	@IRecordField
	Integer		company_id;
	@IRecordField
	Integer		fa_asset_category_id;
	String		balanceSheetCategory;
	@IRecordField
	Integer		fa_bs_category_id;
	@IRecordField
	Boolean		active;

	protected UpdateCompanyFixedAssetCategory() {}	// for serialization only

	public UpdateCompanyFixedAssetCategory(final Integer companyId, final Integer assetCategoryId, final String balanceSheetCategory, final Boolean active) {
		this.company_id = companyId;
		this.fa_asset_category_id = assetCategoryId;
		this.balanceSheetCategory = balanceSheetCategory;
		this.active = active;
	}

	public Integer getCompanyId() {
		return company_id;
	}

	public Integer getAssetCategoryId() {
		return fa_asset_category_id;
	}

	public String getBalanceSheetCategory() {
		return balanceSheetCategory;
	}

	public void setBalanceSheetCategoryId(int id) {
		fa_bs_category_id = id;
	}

	public Boolean getActive() {
		return active;
	}

}
