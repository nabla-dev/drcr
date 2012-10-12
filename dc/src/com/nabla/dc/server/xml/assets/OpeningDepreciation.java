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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
public class OpeningDepreciation extends Node {

	static final String	DATE = "date";
	static final String	ACCUMULATED_DEPRECIATION = "accumulated_depreciation";
	static final String	DEPRECIATION_PERIOD = "depreciation_period";

	@Element(name=DATE)
	Date		date;
	@Element(name=ACCUMULATED_DEPRECIATION)
	Integer		value;
	@Element(name=DEPRECIATION_PERIOD)
	Integer		periodCount;

	@Override
	protected void doValidate(@SuppressWarnings("unused") final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (value <= 0)
			errors.add(getRow(), ACCUMULATED_DEPRECIATION, CommonServerErrors.INVALID_VALUE);
		if (periodCount < 1)
			errors.add(getRow(), DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
	}

	public void postValidate(final XmlAsset asset, final IErrorList<Integer> errors) throws DispatchException {
		final Calendar dtOpening = new GregorianCalendar();
		dtOpening.setTime(date);
		final Calendar dtAcquisition = new GregorianCalendar();
		dtAcquisition.setTime(asset.getAcquisitionDate());
		if (!dtAcquisition.before(dtOpening))
			errors.add(getRow(), DATE, ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);

		final StraightLineDepreciation m = asset.getStraightLineDepreciation();
		if (m.getInitialAccumulatedDepreciation() != null &&
				value <= m.getInitialAccumulatedDepreciation().getValue())
			errors.add(getRow(), ACCUMULATED_DEPRECIATION, ServerErrors.OPENING_MUST_BE_GREATER_THAN_INITIAL_ACCUMULATED_DEPRECIATION);
		if (value > (m.getCost() - m.getResidualValue()))
			errors.add(getRow(), ACCUMULATED_DEPRECIATION, ServerErrors.INVALID_ACCUMULATED_DEPRECIATION);

		if (value < 1)
			errors.add(getRow(), DEPRECIATION_PERIOD, CommonServerErrors.INVALID_VALUE);
		else if (value > asset.getDepreciationPeriod())
			errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
		else if (m.getInitialAccumulatedDepreciation() != null &&
					value <= m.getInitialAccumulatedDepreciation().getPeriodCount())
			errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.DEPRECIATION_PERIOD_LESS_THAN_INITIAL);
		else if (value < (m.getCost() - m.getResidualValue()))
			errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
		else if (value == (m.getCost() - m.getResidualValue()))
			errors.add(getRow(), DEPRECIATION_PERIOD, ServerErrors.OPENING_MUST_BE_LESS_OR_EQUAL_THAN_DEPRECIATION_PERIOD);
	}

	public Date getDate() {
		return date;
	}

	public Integer getValue() {
		return value;
	}

	public Integer getPeriodCount() {
		return periodCount;
	}
}
