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


import com.nabla.dc.shared.command.fixed_asset.FetchAssetTransfer;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class AssetTransferModel extends CModel<Record> {

	static public class Fields {
		public String initialAccumulatedDepreciation() { return IAsset.INITIAL_ACCUMULATED_DEPRECIATION; }
		public String initialDepreciationPeriod() { return IAsset.INITIAL_DEPRECIATION_PERIOD; }
	}

	private static final Fields	fields = new Fields();
	private final int			assetId;

	public AssetTransferModel(final int assetId) {
		this.assetId = assetId;

		setFields(
			new PositiveIntegerField(fields.initialAccumulatedDepreciation(), FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.initialDepreciationPeriod(), FieldAttributes.OPTIONAL)
		);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAssetTransfer(assetId);
	}

}
