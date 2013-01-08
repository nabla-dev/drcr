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
package com.nabla.dc.server.xml.assets;

import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.server.handler.fixed_asset.Asset;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

@Root
public class XmlStraightLineDepreciation extends Node implements IStraightLineDepreciation {

	static final String	OPENING_ACCUMULATED_DEPRECIATION = "opening_accumulated_depreciation";
	static final String	OPENING_DEPRECIATION_PERIOD = "opening_depreciation_period";
	static final String	DATE = "from_date";
	static final String	RESIDUAL_VALUE = "residual_value";

	@Attribute
	Integer		xmlRow;
	@Element(name=OPENING_ACCUMULATED_DEPRECIATION,required=false)
	Integer		openingAccumulatedDepreciation;
	@Element(name=OPENING_DEPRECIATION_PERIOD,required=false)
	Integer		openingDepreciationPeriod;
	@Element(name=DATE,required=false)
	Date		fromDate;
	@Element(name=RESIDUAL_VALUE,required=false)
	Integer		residualValue;

	public Integer getRow() {
		return xmlRow;
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		if (openingAccumulatedDepreciation == null)
			openingAccumulatedDepreciation = 0;
		if (openingDepreciationPeriod == null)
			openingDepreciationPeriod = 0;
		if (residualValue == null)
			residualValue = 0;
		IStraightLineDepreciation.Validator.execute(this, getRow(), ctx.getErrorList());
	}

	public void postValidate(final IAssetRecord asset, final IErrorList<Integer> errors) throws DispatchException {
		IStraightLineDepreciation.Validator.postExecute(this, asset, getRow(), errors);
		Asset.validate(this, asset.getAcquisitionDate(), getRow(), errors);
	}

	@Override
	public int getResidualValue() {
		return residualValue;
	}

	@Override
	public String getResidualValueField() {
		return RESIDUAL_VALUE;
	}

	@Override
	public @Nullable Date getFromDate() {
		return fromDate;
	}

	@Override
	public void setFromDate(Date dt) {
		fromDate = dt;
	}

	@Override
	public String getFromDateField() {
		return DATE;
	}

	@Override
	public int getOpeningAccumulatedDepreciation() {
		return openingAccumulatedDepreciation;
	}

	@Override
	public int getOpeningDepreciationPeriod() {
		return openingDepreciationPeriod;
	}

	@Override
	public String getOpeningAccumulatedDepreciationField() {
		return OPENING_ACCUMULATED_DEPRECIATION;
	}

	@Override
	public String getOpeningDepreciationPeriodField() {
		return OPENING_DEPRECIATION_PERIOD;
	}
}
