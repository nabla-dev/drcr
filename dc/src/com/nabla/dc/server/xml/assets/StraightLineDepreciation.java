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
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class StraightLineDepreciation extends Node {
	@Element
	Integer				cost;
	@Element(required=false)
	InitialDepreciation	initial_accumulated_depreciation;	// if TRANSFER
	@Element(required=false)
	OpeningTransaction	opening_accumulated_depreciation;	// to agree NBV at given period
	@Element(required=false)
	Integer				residual_value;

	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (cost < 0)
			errors.add(COST, CommonServerErrors.INVALID_VALUE);
		if (residualValue < 0)
			errors.add(RESIDUAL_VALUE, CommonServerErrors.INVALID_VALUE);
		else if (residualValue > cost)
			errors.add(RESIDUAL_VALUE, ServerErrors.INVALID_RESIDUAL_VALUE);

		if (acquisition_type == AcquisitionTypes.TRANSFER) {
			if (initialAccumulatedDepreciation == null) {
				if (initialDepreciationPeriod != null)
					errors.add(INITIAL_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
			} else {
				if (initialAccumulatedDepreciation < 0)
					errors.add(INITIAL_ACCUMULATED_DEPRECIATION, CommonServerErrors.INVALID_VALUE);
				else if (initialAccumulatedDepreciation > (cost - residualValue))
					errors.add(INITIAL_ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

				if (initialDepreciationPeriod == null)
					errors.add(INITIAL_DEPRECIATION_PERIOD, CommonServerErrors.REQUIRED_VALUE);
				else if (initialDepreciationPeriod < 0)
					errors.add(INITIAL_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
				else if (initialDepreciationPeriod > depreciation_period)
					errors.add(INITIAL_DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
				else if (initialDepreciationPeriod == depreciation_period) {
					if (initialAccumulatedDepreciation < (cost - residualValue))
						errors.add(INITIAL_DEPRECIATION_PERIOD, ServerErrors.INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
				} if (initialAccumulatedDepreciation == (cost - residualValue))
					errors.add(INITIAL_DEPRECIATION_PERIOD, ServerErrors.INITIAL_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD);
			}
		}

		if (opening) {
			if (openingYear == null)
				errors.add(OPENING_YEAR, CommonServerErrors.REQUIRED_VALUE);
			if (openingMonth == null)
				errors.add(OPENING_MONTH, CommonServerErrors.REQUIRED_VALUE);
			else if (openingMonth < 0 || openingMonth > 11)
				errors.add(OPENING_MONTH, CommonServerErrors.INVALID_VALUE);
			if (openingAccumulatedDepreciation == null)
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, CommonServerErrors.REQUIRED_VALUE);
			else if (openingAccumulatedDepreciation <= getInitialAccumulatedDepreciation())
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION);
			else if (openingAccumulatedDepreciation > (cost - residualValue))
				errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

			if (openingDepreciationPeriod == null)
				errors.add(OPENING_DEPRECIATION_PERIOD, CommonServerErrors.REQUIRED_VALUE);
			else if (openingDepreciationPeriod < 1)
				errors.add(OPENING_DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
			else if (openingDepreciationPeriod > depreciation_period)
				errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
			else if (openingDepreciationPeriod <= getInitialDepreciationPeriod())
				errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
			else if (openingAccumulatedDepreciation != null) {
				if (openingDepreciationPeriod == depreciation_period) {
					if (openingAccumulatedDepreciation < (cost - residualValue))
						errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
				} if (openingAccumulatedDepreciation == (cost - residualValue))
					errors.add(OPENING_DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
			}
		}
	}
}
