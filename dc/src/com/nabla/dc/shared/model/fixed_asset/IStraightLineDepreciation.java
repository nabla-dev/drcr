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
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author FNorais
 *
 */
public interface IStraightLineDepreciation {
	public static class Validator {
		public static <P> boolean execute(final IStraightLineDepreciation t, final P pos, final IErrorList<P> errors) throws DispatchException {
			int n = errors.size();

			if (t.getOpeningAccumulatedDepreciation() < 0)
				errors.add(pos, t.getOpeningAccumulatedDepreciationField(), CommonServerErrors.INVALID_VALUE);

			if (t.getOpeningDepreciationPeriodCount() < 0)
				errors.add(pos, t.getOpeningDepreciationPeriodCountField(), CommonServerErrors.INVALID_VALUE);
			if (t.getOpeningAccumulatedDepreciation() > 0 && t.getOpeningDepreciationPeriodCount() < 1)
				errors.add(pos, t.getOpeningDepreciationPeriodCountField(), CommonServerErrors.INVALID_VALUE);

			if (t.getResidualValue() < 0)
				errors.add(pos, t.getResidualValueField(), CommonServerErrors.INVALID_VALUE);

			return n == errors.size();
		}
		public static <P> boolean postExecute(final IStraightLineDepreciation t, final IAssetRecord asset, final P pos, final IErrorList<P> errors) throws DispatchException {
			int n = errors.size();

			if (t.getOpeningAccumulatedDepreciation() > asset.getTotalDepreciation())
				errors.add(pos, t.getOpeningAccumulatedDepreciationField(), ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);
			else if (t.getOpeningAccumulatedDepreciation() < asset.getTotalDepreciation()) {
				if (t.getOpeningDepreciationPeriodCount() >= asset.getDepreciationPeriod())
					errors.add(pos, t.getOpeningDepreciationPeriodCountField(), ServerErrors.INITIAL_MUST_BE_LESS_THAN_DEPRECIATION_PERIOD);
			} else /*if (t.getValue() == asset.getTotalDepreciation())*/ {
				if (t.getOpeningDepreciationPeriodCount() != asset.getDepreciationPeriod())
					errors.add(pos, t.getOpeningDepreciationPeriodCountField(), ServerErrors.INITIAL_MUST_BE_EQUAL_TO_DEPRECIATION_PERIOD);
			}

			if (t.getResidualValue() > asset.getCost())
				errors.add(pos, t.getResidualValueField(), ServerErrors.INVALID_RESIDUAL_VALUE);

			return n == errors.size();
		}
	}

	@Nullable Date getFromDate();
	void setFromDate(final Date dt);
	String getFromDateField();

	Integer getOpeningAccumulatedDepreciation();
	Integer getOpeningDepreciationPeriodCount();

	String getOpeningAccumulatedDepreciationField();
	String getOpeningDepreciationPeriodCountField();

	Integer getResidualValue();
	String getResidualValueField();
}
