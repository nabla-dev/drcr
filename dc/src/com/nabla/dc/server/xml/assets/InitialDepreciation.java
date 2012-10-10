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
import com.nabla.wapp.server.xml.XmlInteger;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class InitialDepreciation extends Node {

	@Element
	XmlInteger	accumulated_depreciation;
	@Element
	XmlInteger	depreciation_period;

	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
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
}
