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

import com.nabla.dc.shared.command.fixed_asset.FetchAssetDisposal;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PoundField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;


public class AssetDisposalModel extends BasicAssetModel {

	private final Integer	assetId;

	public AssetDisposalModel(final Integer assetId) {
		super();

		this.assetId = assetId;
		setFields(
			new IdField(),
			new TextField(fields.name(), FieldAttributes.READ_ONLY),
			new DateField(fields.disposalDate(), FieldAttributes.REQUIRED),
			new DisposalTypeField(fields.disposalType(), FieldAttributes.REQUIRED),
			new PoundField(fields.proceeds(), FieldAttributes.OPTIONAL)
		);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAssetDisposal(assetId);
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final AssetRecord record) {
		return record.toDisposalCommand();
	}

}
