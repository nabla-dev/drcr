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
package com.nabla.dc.client.model.settings.fixed_asset;


import com.nabla.dc.shared.command.fixed_asset.AddFixedAssetCategory;
import com.nabla.dc.shared.command.fixed_asset.FetchFixedAssetCategoryList;
import com.nabla.dc.shared.command.fixed_asset.RemoveFixedAssetCategory;
import com.nabla.dc.shared.command.fixed_asset.UpdateFixedAssetCategory;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.DeletedRecordField;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.IntegerSpinnerField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class FixedAssetCategoryListModel extends CModel<FixedAssetCategoryRecord> {

	static public class Fields {
		public String name() { return IFixedAssetCategory.NAME; }
		public String type() { return IFixedAssetCategory.TYPE; }
		public String active() { return IFixedAssetCategory.ACTIVE; }
		public String minPeriod() { return IFixedAssetCategory.MIN_DEPRECIATION_PERIOD; }
		public String maxPeriod() { return IFixedAssetCategory.MAX_DEPRECIATION_PERIOD; }
	}

	private static final Fields	fields = new Fields();

	public FixedAssetCategoryListModel() {
		super(FixedAssetCategoryRecord.factory);

		setFields(
			new DeletedRecordField(),
			new IdField(),
			new TextField(fields.name(), IFixedAssetCategory.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new AssetCategoryTypeField(fields.type()),
			new BooleanField(fields.active()),
			new IntegerSpinnerField(fields.minPeriod(), IFixedAssetCategory.DEFAULT_DEPRECIATION_PERIOD, IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT, FieldAttributes.REQUIRED),
			new IntegerSpinnerField(fields.maxPeriod(), IFixedAssetCategory.DEFAULT_DEPRECIATION_PERIOD, IFixedAssetCategory.DEPRECIATION_PERIOD_CONSTRAINT, FieldAttributes.REQUIRED)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveFixedAssetCategory();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchFixedAssetCategoryList();
	}

	@Override
	public IAction<StringResult> getAddCommand(final FixedAssetCategoryRecord record) {
		return new AddFixedAssetCategory(record.getName(), record.getActive(), record.getType(), record.getMinDepreciationPeriod(), record.getMaxDepreciationPeriod());
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final FixedAssetCategoryRecord record) {
		return new UpdateFixedAssetCategory(record.getId(), record.getName(), record.getActive(), record.getType(), record.getMinDepreciationPeriod(), record.getMaxDepreciationPeriod());
	}

}
