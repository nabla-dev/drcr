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
package com.nabla.dc.shared.command.fixed_asset.settings;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.model.IErrorList;


/**
 * @author nabla
 *
 */
public class UpdateFixedAssetCategory extends AddFixedAssetCategory {

	@IRecordField(id=true)
	Integer		id;

	protected UpdateFixedAssetCategory() {}	// for serialization only

	public UpdateFixedAssetCategory(final Integer id, final String name, final Boolean active, final FixedAssetCategoryTypes type, final Integer min_depreciation_period, final Integer max_depreciation_period) {
		super(name, active, type, min_depreciation_period, max_depreciation_period);
		this.id = id;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		int n = errors.size();
		if (name != null && NAME_CONSTRAINT.validate(NAME, name, errors))
			uname = name.toUpperCase();
		if (min_depreciation_period != null)
			DEPRECIATION_PERIOD_CONSTRAINT.validate(MIN_DEPRECIATION_PERIOD, min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors);
		if (max_depreciation_period != null)
			DEPRECIATION_PERIOD_CONSTRAINT.validate(MAX_DEPRECIATION_PERIOD, max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors);
		if (max_depreciation_period != null && min_depreciation_period != null &&
			min_depreciation_period > max_depreciation_period)
			errors.add(MAX_DEPRECIATION_PERIOD, ServerErrors.INVALID_DEPRECIATION_PERIOD);
		return n == errors.size();
	}

	public Integer getId() {
		return id;
	}
}
