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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.server.handler.fixed_asset.Asset;
import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

@Root
public class XmlDisposal extends Node implements IDisposal {

	static final String	DATE = "date";
	static final String	TYPE = "type";
	static final String	PROCEEDS = "proceeds";

	@Attribute
	Integer			xmlRow;
	@Element(name=DATE)
	Date			date;
	@Element(name=TYPE, required=false)
	DisposalTypes	type;
	@Element(name=PROCEEDS, required=false) @Nullable
	Integer			proceeds;

	public Integer getRow() {
		return xmlRow;
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		if (type == null)
			type = DisposalTypes.SOLD;
		Validator.execute(this, getRow(), ctx.getErrorList());
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
