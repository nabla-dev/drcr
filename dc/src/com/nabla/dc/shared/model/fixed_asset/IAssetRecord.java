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

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
public interface IAssetRecord {
	static class Validator {
		public static <P> boolean execute(final IAssetRecord t, final P pos, final IErrorList<P> errors) throws DispatchException {
			int n = errors.size();

			if (t.getCost() == null)
				errors.add(pos, t.getCostField(), CommonServerErrors.REQUIRED_VALUE);
			else if (t.getCost() < 0)
				errors.add(pos, t.getCostField(), CommonServerErrors.INVALID_VALUE);

			return n == errors.size();
		}
	}

	Integer getCompanyAssetCategoryId();
	String getName();
	Date getAcquisitionDate();
	Integer getDepreciationPeriod();
	Integer getCost();
	Integer getTotalDepreciation();	// i.e. Cost - ResidualValue

	@Nullable IInitialDepreciation getInitialDepreciation();
	@Nullable IOpeningDepreciation getOpeningDepreciation();
	@Nullable IStraightLineDepreciation getDepreciationMethod();
	@Nullable IDisposal getDisposal();

	String getCostField();
	String getCategoryField();
	String getDepreciationPeriodField();
}
