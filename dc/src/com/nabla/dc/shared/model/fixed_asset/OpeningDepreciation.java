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

import java.util.Date;

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;

public class OpeningDepreciation extends AccumulatedDepreciation implements IOpeningDepreciation {
	final Date		date;

	public OpeningDepreciation(final Date date, final Integer value, final Integer periodCount) {
		super(value, periodCount);
		this.date = date;
	}

	@Override
	public Date getDate() {
		return date;
	}

	public boolean validate(final IAssetRecord asset, final IErrorList<Void> errors) throws DispatchException {
		return Validator.execute(this, null, errors) &&
				Validator.postExecute(this, asset, null, errors);
	}

	@Override
	public String getValueField() {
		return IAsset.OPENING_ACCUMULATED_DEPRECIATION;
	}

	@Override
	public String getPeriodCountField() {
		return IAsset.OPENING_DEPRECIATION_PERIOD;
	}

	@Override
	public String getDateField() {
		return IAsset.OPENING_YEAR;
	}
}