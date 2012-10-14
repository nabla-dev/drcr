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

import com.nabla.dc.shared.ServerErrors;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

public interface IOpeningDepreciation extends IAccumulatedDepreciation {
	public static class Validator {
		@SuppressWarnings("deprecation")
		public static <P> boolean execute(final IOpeningDepreciation t, final P pos, final IErrorList<P> errors) throws DispatchException {
			int n = errors.size();

			if (t.getDate() == null)
				errors.add(pos, t.getDateField(), CommonServerErrors.REQUIRED_VALUE);
			else if (t.getDate().getDay() != 1)
				errors.add(pos, t.getDateField(), CommonServerErrors.INVALID_VALUE);

			IAccumulatedDepreciation.Validator.execute(t, pos, errors);

			return n == errors.size();
		}
		public static <P> boolean postExecute(final IOpeningDepreciation t, final IAssetRecord asset, final P pos, final IErrorList<P> errors) throws DispatchException {
			int n = errors.size();

			if (t.getValue() > asset.getTotalDepreciation())
				errors.add(pos, t.getValueField(), ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);
			else if (t.getValue() < asset.getTotalDepreciation()) {
				if (t.getPeriodCount() >= asset.getDepreciationPeriod())
					errors.add(pos, t.getPeriodCountField(), ServerErrors.OPENING_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
			} else /*if (t.getValue() == asset.getTotalDepreciation())*/ {
				if (t.getPeriodCount() != asset.getDepreciationPeriod())
					errors.add(pos, t.getPeriodCountField(), ServerErrors.INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
			}

			final IInitialDepreciation init = asset.getInitialDepreciation();
			if (init != null) {
				if (t.getValue() <= init.getValue())
					errors.add(pos, t.getValueField(), ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION);
				if (t.getPeriodCount() <= init.getPeriodCount())
					errors.add(pos, t.getPeriodCountField(), ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_DEPRECIATION_PERIOD);
			}

			return n == errors.size();
		}
	}

	Date getDate();

	String getDateField();
}
