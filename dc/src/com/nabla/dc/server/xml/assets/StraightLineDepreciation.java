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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class StraightLineDepreciation extends Node {

	static final String	COST = "cost";
	static final String	INITIAL_ACCUMULATED_DEPRECIATION = "initial_accumulated_depreciation";
	static final String	OPENING_ACCUMULATED_DEPRECAITION = "opening_accumulated_depreciation";
	static final String	RESIDUAL_VALUE = "residual_value";

	@Element(name=COST)
	Integer				cost;
	@Element(name=INITIAL_ACCUMULATED_DEPRECIATION,required=false)
	InitialDepreciation	initialDepreciation;	// if TRANSFER
	@Element(name=OPENING_ACCUMULATED_DEPRECAITION,required=false)
	OpeningDepreciation	openingDepreciation;	// to agree NBV at given period
	@Element(name=RESIDUAL_VALUE,required=false)
	Integer				residualValue;

	@Override
	protected void doValidate(@SuppressWarnings("unused") final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (cost < 0)
			errors.add(getRow(), COST, CommonServerErrors.INVALID_VALUE);
		if (residualValue == null)
			residualValue = 0;
		else if (residualValue < 0)
			errors.add(getRow(), RESIDUAL_VALUE, CommonServerErrors.INVALID_VALUE);
		else if (residualValue > cost)
			errors.add(getRow(), RESIDUAL_VALUE, ServerErrors.INVALID_RESIDUAL_VALUE);
	}

	public void postValidate(final XmlAsset asset, final IErrorList<Integer> errors) throws DispatchException {
		if (initialDepreciation != null)
			initialDepreciation.postValidate(asset.getDepreciationPeriod(), this, errors);
		if (openingDepreciation != null)
			openingDepreciation.postValidate(asset, errors);
	}

	public Integer getCost() {
		return cost;
	}

	public @Nullable InitialDepreciation getInitialAccumulatedDepreciation() {
		return initialDepreciation;
	}

	public @Nullable OpeningDepreciation getOpeningAccumulatedDepreciation() {
		return openingDepreciation;
	}

	public Integer getResidualValue() {
		return residualValue;
	}
}
