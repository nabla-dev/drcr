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

import com.nabla.dc.shared.command.fixed_asset.FetchSplitAsset;
import com.nabla.dc.shared.command.fixed_asset.SplitAsset;
import com.nabla.dc.shared.model.fixed_asset.ISplitAsset;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.validator.TextLengthValidator;
import com.nabla.wapp.client.model.validator.ValidatorList;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class SplitAssetModel extends CModel<SplitAssetRecord> {

	static public class Fields implements ISplitAsset {
		public String nameA() { return NAME_A; }
		public String nameB() { return NAME_B; }
		public String referenceA() { return REFERENCE_A; }
		public String referenceB() { return REFERENCE_B; }

		public String costA() { return COST_A; }
		public String costB() { return COST_B; }

		public String total() { return TOTAL; }
	}

	private static final Fields	fields = new Fields();
	private final int				assetId;

	public SplitAssetModel(final int assetId) {
		super(SplitAssetRecord.factory);

		this.assetId = assetId;
		setFields(
			new IdField(),
			new TextField(fields.nameA(), ISplitAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.nameB(), new ValidatorList(new TextLengthValidator(ISplitAsset.NAME_CONSTRAINT)), FieldAttributes.REQUIRED),
			new TextField(fields.referenceA(), ISplitAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(fields.referenceB(), ISplitAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.costA(), FieldAttributes.REQUIRED),
			new PositiveIntegerField(fields.costB(), FieldAttributes.REQUIRED),
			new PositiveIntegerField(fields.total(), FieldAttributes.REQUIRED)
		);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchSplitAsset(assetId);
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final SplitAssetRecord record) {
		return new SplitAsset(record.getId(), record.getNameA(), record.getNameB(), record.getReferenceA(), record.getReferenceB(), record.getCostA(), record.getTotal());
	}

}
