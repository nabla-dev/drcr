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


import com.nabla.dc.shared.command.fixed_asset.RemoveAsset;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.PoundField;
import com.nabla.wapp.client.model.field.SelectBoxField;
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
public class AssetListModel extends CModel<AssetRecord> {

	static public class Fields {
		public String name() { return IAsset.NAME; }
		public String category() { return IAsset.CATEGORY; }
		public String reference() { return IAsset.REFERENCE; }
		public String location() { return IAsset.LOCATION; }
		public String pi() { return IAsset.PI; }

		public String cost() { return IAsset.COST; }
		public String depPeriod() { return IAsset.DEP_PERIOD; }

		public String acquisitionDate() { return IAsset.ACQUISITION_DATE; }
		public String acquisitionType() { return IAsset.ACQUISITION_TYPE; }

		public String disposalDate() { return IAsset.DISPOSAL_DATE; }
		public String proceeds() { return IAsset.PROCEEDS; }
	}

	private static final Fields	fields = new Fields();
	private final Integer			companyId;
	private boolean		enableUpdate = true;

	public AssetListModel(final Integer companyId) {
		super(AssetRecord.factory);

		this.companyId = companyId;
		setFields(
			new IdField(),

			new TextField(IAsset.NAME, IAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new SelectBoxField(IAsset.CATEGORY, categoryModelFactory.get(assetRegisterId), IdField.NAME, IAssetCategory.NAME, FieldAttributes.REQUIRED),
			new TextField(IAsset.REFERENCE, IAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(IAsset.LOCATION, IAsset.LOCATION_CONSTRAINT, FieldAttributes.OPTIONAL),

			new DateField(IAsset.ACQUISITION_DATE, FieldAttributes.REQUIRED),
			new AcquisitionTypeField(IAsset.ACQUISITION_TYPE, FieldAttributes.REQUIRED),
			new PoundField(IAsset.COST, FieldAttributes.REQUIRED),
			new TextField(IAsset.PI, IAsset.PI_CONSTRAINT, FieldAttributes.OPTIONAL),

			new PositiveIntegerField(IAsset.DEP_PERIOD, FieldAttributes.REQUIRED),

			new DateField(IAsset.DISPOSAL_DATE, FieldAttributes.OPTIONAL),
			new PoundField(IAsset.PROCEEDS, FieldAttributes.OPTIONAL)
				);
	}

	public void setEnableUpdate(boolean enable) {
		this.enableUpdate = enable;
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAssetList(companyId);
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveAsset();
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final AssetRecord record) {
		final IAction<StringResult> ret = enableUpdate ? new UpdateAssetField(record.getId()) : null;
		enableUpdate = true;
		return ret;
	}

}
