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

import com.nabla.dc.shared.ServerErrors;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;


/**
 * @author FNorais
 *
 */
public interface IInitialDepreciation extends IAccumulatedDepreciation {
	public static class Validator {
		public static <P> boolean execute(final IAccumulatedDepreciation t, final P pos, final IErrorList<P> errors) throws DispatchException {
			return IAccumulatedDepreciation.Validator.execute(t, pos, errors);
		}

		public static <P> boolean postExecute(final IInitialDepreciation t, final IAssetRecord asset, final P pos, final IErrorList<P> errors) throws DispatchException {
			int n = errors.size();

			if (t.getValue() > asset.getTotalDepreciation())
				errors.add(pos, t.getValueField(), ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);
			else if (t.getValue() < asset.getTotalDepreciation()) {
				if (t.getPeriodCount() >= asset.getDepreciationPeriod())
					errors.add(pos, t.getPeriodCountField(), ServerErrors.INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
			} else /*if (t.getValue() == asset.getTotalDepreciation())*/ {
				if (t.getPeriodCount() != asset.getDepreciationPeriod())
					errors.add(pos, t.getPeriodCountField(), ServerErrors.INITIAL_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD);
			}

			return n == errors.size();
		}
	}
}
