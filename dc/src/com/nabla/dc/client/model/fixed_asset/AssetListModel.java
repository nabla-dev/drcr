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


import java.util.Set;

import com.nabla.dc.shared.command.fixed_asset.FetchAssetList;
import com.nabla.dc.shared.command.fixed_asset.FetchAssetRecord;
import com.nabla.dc.shared.command.fixed_asset.RemoveAsset;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.PoundField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;


public class AssetListModel extends BasicAssetModel {

	private final Integer	companyId;

	public AssetListModel(final Integer companyId) {
		super();

		this.companyId = companyId;
		setFields(
			new IdField(),

			new TextField(fields.name(), IAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.category(), FieldAttributes.REQUIRED),
			new TextField(fields.reference(), IAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(fields.location(), IAsset.LOCATION_CONSTRAINT, FieldAttributes.OPTIONAL),

			new DateField(fields.acquisitionDate(), FieldAttributes.REQUIRED),
			new AcquisitionTypeField(fields.acquisitionType(), FieldAttributes.REQUIRED),
			new PoundField(fields.cost(), FieldAttributes.REQUIRED),
			new TextField(fields.pi(), IAsset.PURCHASE_INVOICE_CONSTRAINT, FieldAttributes.OPTIONAL),

			new PositiveIntegerField(fields.depPeriod(), FieldAttributes.REQUIRED),

			new DateField(fields.disposalDate(), FieldAttributes.OPTIONAL),
			new PoundField(fields.proceeds(), FieldAttributes.OPTIONAL)
				);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAssetList(companyId);
	}

	@Override
	public AbstractFetch getFetchRecordCommand(final Set<Integer> ids) {
		return new FetchAssetRecord(ids);
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveAsset();
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final AssetRecord record) {
		return record.toUpdateFieldCommand();
	}

}
