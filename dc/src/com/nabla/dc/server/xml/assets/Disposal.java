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
import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class Disposal extends Node {

	static final String	DATE = "date";
	static final String	TYPE = "type";
	static final String	PROCEEDS = "proceeds";

	@Element(name=DATE)
	Date			date;
	@Element(name=TYPE, required=false)
	DisposalTypes	type;
	@Element(name=PROCEEDS, required=false) @Nullable
	Integer			proceeds;

	@Override
	protected void doValidate(@SuppressWarnings("unused") final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (type == null)
			type = DisposalTypes.SOLD;
		switch (type) {
		case SOLD:
			if (proceeds == null)
				proceeds = 0;
			else if (proceeds < 0)
				errors.add(getRow(), PROCEEDS, CommonServerErrors.INVALID_VALUE);
			break;
		default:
			proceeds = null;
			break;
		}
	}

	public void postValidate(final XmlAsset asset, final IErrorList<Integer> errors) throws DispatchException {
		// validate disposal date: must be at least in following month of acquisition!
		final Calendar dt = new GregorianCalendar();
		dt.setTime(asset.getAcquisitionDate());
		dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		final Calendar dtDisposal = new GregorianCalendar();
		dtDisposal.setTime(date);
		if (!dt.before(dtDisposal))
			errors.add(getRow(), DATE, ServerErrors.MUST_BE_AFTER_ACQUISITION_DATE);
		// same with opening date
		final StraightLineDepreciation depreciation = asset.getStraightLineDepreciation();
		if (depreciation != null && depreciation.getOpeningAccumulatedDepreciation() != null) {
			dt.setTime(depreciation.getOpeningAccumulatedDepreciation().getDate());
			dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
			if (dtDisposal.before(dt))
				errors.add(getRow(), DATE, ServerErrors.MUST_BE_AFTER_OPENING_DATE);
		}
	}

	public Date getDate() {
		return date;
	}

	public DisposalTypes getType() {
		return type;
	}

	public @Nullable Integer getProceeds() {
		return proceeds;
	}

}
