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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
public class StraightlineDepreciation implements IsSerializable, IStraightLineDepreciation{

	Integer			openingAccumulatedDepreciation;
	Integer			openingDepreciationPeriodCount;
	@Nullable Date	fromDate;
	Integer			residualValue;

	StraightlineDepreciation() {}	// for serialization only

	public StraightlineDepreciation(final Integer openingAccumulatedDepreciation, final Integer openingDepreciationPeriodCount, final Date fromDate, final Integer residualValue) {
		this.openingAccumulatedDepreciation = (openingAccumulatedDepreciation == null) ? 0 : openingAccumulatedDepreciation;
		this.openingDepreciationPeriodCount = (openingDepreciationPeriodCount == null) ? 0 : openingDepreciationPeriodCount;
		this.fromDate = fromDate;
		this.residualValue = (residualValue == null) ? 0 : residualValue;
	}

	public boolean validate(final IAssetRecord asset, final IErrorList<Void> errors) throws DispatchException {
		return Validator.execute(this, null, errors) &&
				Validator.postExecute(this, asset, null, errors);
	}

	@Override
	public Integer getResidualValue() {
		return residualValue;
	}

	@Override
	public String getResidualValueField() {
		return IAsset.RESIDUAL_VALUE;
	}

	@Override
	public @Nullable Date getFromDate() {
		return fromDate;
	}

	@Override
	public void setFromDate(Date dt) {
		this.fromDate = dt;
	}

	@Override
	public String getFromDateField() {
		return IAsset.DEPRECIATION_FROM_DATE;
	}

	@Override
	public Integer getOpeningAccumulatedDepreciation() {
		return openingAccumulatedDepreciation;
	}

	@Override
	public Integer getOpeningDepreciationPeriodCount() {
		return openingDepreciationPeriodCount;
	}

	@Override
	public String getOpeningAccumulatedDepreciationField() {
		return IAsset.OPENING_ACCUMULATED_DEPRECIATION;
	}

	@Override
	public String getOpeningDepreciationPeriodCountField() {
		return IAsset.OPENING_DEPRECIATION_PERIOD;
	}

}
