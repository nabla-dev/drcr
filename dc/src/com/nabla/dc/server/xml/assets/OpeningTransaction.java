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
public class OpeningTransaction extends Node {
	@Element
	Date		date;
	@Element
	Integer		accumulated_depreciation;
	@Element
	Integer		depreciation_period;

	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		else if (openingAccumulatedDepreciation <= getInitialAccumulatedDepreciation())
			errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION);
		else if (openingAccumulatedDepreciation > (cost - residualValue))
			errors.add(OPENING_ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

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
