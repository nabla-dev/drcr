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

import com.nabla.dc.server.handler.fixed_asset.Asset;
import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IOpeningDepreciation;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class XmlOpeningDepreciation extends Node implements IOpeningDepreciation {

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
		Validator.execute(this, getRow(), errors);
	}

	public boolean postValidate(final IAssetRecord asset, final IErrorList<Integer> errors) throws DispatchException {
		return Validator.postExecute(this, asset, getRow(), errors) &&
				Asset.validate(this, asset.getAcquisitionDate(), getRow(), errors);
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public Integer getPeriodCount() {
		return periodCount;
	}

	@Override
	public String getValueField() {
		return ACCUMULATED_DEPRECIATION;
	}

	@Override
	public String getPeriodCountField() {
		return DEPRECIATION_PERIOD;
	}

	@Override
	public String getDateField() {
		return DATE;
	}
}
