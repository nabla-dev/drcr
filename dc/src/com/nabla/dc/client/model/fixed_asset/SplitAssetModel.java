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

import com.nabla.dc.shared.command.fixed_asset.SplitAsset;
import com.nabla.dc.shared.model.fixed_asset.ISplitAsset;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.TextField;
import com.smartgwt.client.types.DSOperationType;

/**
 * @author nabla
 *
 */
public class SplitAssetModel extends CModel<AssetRecord> {

	static public class Fields implements ISplitAsset {
		public String nameA() { return NAME_A; }
		public String nameB() { return NAME_B; }
		public String referenceA() { return REFERENCE_A; }
		public String referenceB() { return REFERENCE_B; }

		public String costA() { return COST_A; }
		public String costB() { return COST_B; }

		public String total() { return "total"; }
	}

	private static final Fields	fields = new Fields();
	private final int				assetId;

	public SplitAssetModel(final int assetId) {
		super();

		this.assetId = assetId;
		setFields(
			new IdField(),
			new TextField(fields.nameA(), ISplitAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.nameB(), ISplitAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.referenceA(), ISplitAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(fields.referenceB(), ISplitAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.costA(), FieldAttributes.REQUIRED)
		);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractOperationAction getCommand(@SuppressWarnings("unused") final DSOperationType op) {
		return (op == DSOperationType.UPDATE) ? new SplitAsset() : null;
	}

}
