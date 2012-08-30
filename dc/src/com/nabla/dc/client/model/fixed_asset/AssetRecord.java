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
package com.nabla.dc.client.model.fixed_asset;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.JSHelper;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.JSOHelper;

/**
 * @author nabla
 *
 */
public class AssetRecord extends BasicListGridRecord implements IAsset {

	public static final IRecordFactory<AssetRecord>	factory = new IRecordFactory<AssetRecord>() {
		@Override
		public AssetRecord get(final JavaScriptObject data) {
			return new AssetRecord(data);
		}
	};

	public AssetRecord(final Record impl) {
		super(impl);
	}

	public AssetRecord(final JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(IAsset.NAME);
	}

	public Boolean isDisposed() {
		String date = getAttributeAsString(IAsset.DISPOSAL_DATE);
		return date != null && !date.isEmpty();
	}

	public void setDisposal(final Record disposal) {
		JSHelper.copyAttribute(disposal.getJsObj(), getJsObj(), IAsset.DISPOSAL_DATE);
		JSHelper.copyAttribute(disposal.getJsObj(), getJsObj(), IAsset.PROCEEDS);
	}

	public void clearDisposal() {
		JSOHelper.deleteAttributeIfExists(getJsObj(), IAsset.DISPOSAL_DATE);
		JSOHelper.deleteAttributeIfExists(getJsObj(), IAsset.PROCEEDS);
	}

	public AcquisitionTypes getAcquisitionType() {
		return AcquisitionTypes.valueOf(getAttributeAsString(IAsset.ACQUISITION_TYPE));
	}

	public boolean isTransfer() {
		return getAcquisitionType() == AcquisitionTypes.TRANSFER;
	}

	public Integer getCost() {
		return getAttributeAsInt(IAsset.COST);
	}

	public boolean hasCost() {
		final Integer cost = getCost();
		return cost != null && cost > 0;
	}

	public String getReference() {
		return getAttributeAsString(IAsset.REFERENCE);
	}

}
