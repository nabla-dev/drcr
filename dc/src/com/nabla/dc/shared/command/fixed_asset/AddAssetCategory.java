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

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.AssetCategoryTypes;
import com.nabla.dc.shared.model.fixed_asset.IAssetCategory;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAssetCategory.TABLE)
public class AddAssetCategory implements IRecordAction<StringResult>, IAssetCategory {

	@IRecordField(unique=true)
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField
	Boolean				active;
	@IRecordField
	AssetCategoryTypes	type;
	@IRecordField
	Integer				min_depreciation_period;
	@IRecordField
	Integer				max_depreciation_period;

	protected AddAssetCategory() {}	// for serialization only

	public AddAssetCategory(final String name, final Boolean active, final AssetCategoryTypes type, final Integer min_depreciation_period) {
		this.name = name;
		this.active = active;
		this.type = type;
		this.min_depreciation_period = min_depreciation_period;
	}

	public AddAssetCategory(final String name, final Boolean active, final AssetCategoryTypes type, final Integer min_depreciation_period, final Integer max_depreciation_period) {
		this(name, active, type, min_depreciation_period);
		this.max_depreciation_period = max_depreciation_period;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		int n = errors.size();
		if (NAME_CONSTRAINT.validate(NAME, name, errors))
			uname = name.toUpperCase();
		DEPRECIATION_PERIOD_CONSTRAINT.validate(MIN_DEPRECIATION_PERIOD, min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors);
		if (max_depreciation_period == null)
			max_depreciation_period = min_depreciation_period;
		else if (DEPRECIATION_PERIOD_CONSTRAINT.validate(MAX_DEPRECIATION_PERIOD, max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors) &&
			max_depreciation_period < min_depreciation_period)
				errors.add(MAX_DEPRECIATION_PERIOD, ServerErrors.INVALID_MAX_DEPRECIATION_PERIOD);
		if (active == null)
			active = false;
		if (type == null)
			type = AssetCategoryTypes.TANGIBLE;
		return n == errors.size();
	}

	public Boolean getActive() {
		return active;
	}

	public AssetCategoryTypes getType() {
		return type;
	}

	public Integer getMinDepreciationPeriod() {
		return min_depreciation_period;
	}

	public Integer getMaxDepreciationPeriod() {
		return max_depreciation_period;
	}

}
