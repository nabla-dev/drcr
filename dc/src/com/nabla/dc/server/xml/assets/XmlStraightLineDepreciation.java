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

import com.nabla.dc.shared.model.fixed_asset.IAssetRecord;
import com.nabla.dc.shared.model.fixed_asset.IStraightLineDepreciation;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
@Root
public class XmlStraightLineDepreciation extends Node implements IStraightLineDepreciation {

	static final String	RESIDUAL_VALUE = "residual_value";

	@Element(name=RESIDUAL_VALUE,required=false)
	Integer		residualValue;

	@Override
	protected void doValidate(@SuppressWarnings("unused") final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		if (residualValue == null)
			residualValue = 0;
		IStraightLineDepreciation.Validator.execute(this, getRow(), errors);
	}

	public void postValidate(final IAssetRecord asset, final IErrorList<Integer> errors) throws DispatchException {
		IStraightLineDepreciation.Validator.postExecute(this, asset, getRow(), errors);
	}

	@Override
	public Integer getResidualValue() {
		return residualValue;
	}

	@Override
	public String getResidualValueField() {
		return RESIDUAL_VALUE;
	}
}
