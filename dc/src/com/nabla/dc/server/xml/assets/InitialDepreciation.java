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
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class InitialDepreciation extends Node {

	static final String	ACCUMULATED_DEPRECIATION = "accumulated_depreciation";
	static final String	DEPRECIATION_PERIOD = "depreciation_period";

	@Element(name=ACCUMULATED_DEPRECIATION)
	Integer	value;
	@Element(name=DEPRECIATION_PERIOD)
	Integer	periodCount;

	@Override
	protected void doValidate(@SuppressWarnings("unused") final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (value < 1)
			errors.add(getRow(), ACCUMULATED_DEPRECIATION, CommonServerErrors.INVALID_VALUE);
		if (periodCount < 1)
			errors.add(getRow(), DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
	}

	public void postValidate(final Integer depreciationPeriod, final StraightLineDepreciation m, final IErrorList<Integer> errors) throws DispatchException {
		if (value > (m.getCost() - m.getResidualValue()))
			errors.add(getRow(), ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

		if (periodCount >  depreciationPeriod)
			errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
		else if (periodCount == depreciationPeriod) {
			if (value < (m.getCost() - m.getResidualValue()))
				errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
		} if (value == (m.getCost() - m.getResidualValue()))
			errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.INITIAL_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD);
	}

	public Integer getValue() {
		return value;
	}

	public Integer getPeriodCount() {
		return periodCount;
	}

}
