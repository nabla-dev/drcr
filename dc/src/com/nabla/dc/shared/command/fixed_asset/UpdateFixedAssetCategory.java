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

import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;



public class UpdateFixedAssetCategory extends AddFixedAssetCategory {

	@IRecordField(id=true)
	int		id;

	protected UpdateFixedAssetCategory() {}	// for serialization only

	public UpdateFixedAssetCategory(final int id, final String name, final Boolean active, final FixedAssetCategoryTypes type, final Integer min_depreciation_period, final Integer max_depreciation_period) {
		super(name, active, type, min_depreciation_period, max_depreciation_period);
		this.id = id;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.UPDATE);
	}

	public int getId() {
		return id;
	}

}
