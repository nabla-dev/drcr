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
import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class XmlDisposal extends Node implements IDisposal {

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
		Validator.execute(this, getRow(), errors);
	}

	public void postValidate(final Date dtAcquisition, final IErrorList<Integer> errors) throws DispatchException {
		Asset.validate(this, dtAcquisition, getRow(), errors);
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public DisposalTypes getType() {
		return type;
	}

	@Override
	public @Nullable Integer getProceeds() {
		return proceeds;
	}

	@Override
	public void setProceeds(@Nullable Integer value) {
		proceeds = value;
	}

	@Override
	public String getDateField() {
		return DATE;
	}

	@Override
	public String getProceedsField() {
		return PROCEEDS;
	}

}
