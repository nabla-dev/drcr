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


import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.IntegerField;
import com.nabla.wapp.client.model.field.IntegerSpinnerField;
import com.nabla.wapp.client.model.field.MonthField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.SelectBoxField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class AssetWizardModel extends CModel<AssetRecord> {

	static public class Fields {
		public String name() { return IAsset.NAME; }
		public String category() { return IAsset.CATEGORY; }
		public String reference() { return IAsset.REFERENCE; }
		public String location() { return IAsset.LOCATION; }

		public String acquisitionDate() { return IAsset.ACQUISITION_DATE; }
		public String acquisitionType() { return IAsset.ACQUISITION_TYPE; }
		public String cost() { return IAsset.COST; }
		public String pi() { return IAsset.PURCHASE_INVOICE; }
		public String initialAccumDep() { return IAsset.INITIAL_ACCUM_DEPRECIATION; }
		public String initialDepPeriod() { return IAsset.INITIAL_DEPRECIATION_PERIOD; }

		public String depPeriod() { return IAsset.DEPRECIATION_PERIOD; }
		public String residualValue() { return IAsset.RESIDUAL_VALUE; }
		public String createTransactions() { return IAsset.CREATE_TRANSACTIONS; }
		public String opening() { return IAsset.OPENING; }
		public String openingMonth() { return IAsset.OPENING_MONTH; }
		public String openingYear() { return IAsset.OPENING_YEAR; }
		public String openingAccumDep() { return IAsset.OPENING_ACCUM_DEPRECIATION; }
		public String openingDepPeriod() { return IAsset.OPENING_DEPRECIATION_PERIOD; }
	}

	private final Integer	assetId;

	public AssetWizardModel(final Integer companyId, final Integer assetId) {
		super(AssetRecord.factory);

		this.assetId = assetId;
		setFields(
			new IdField(),

			new TextField(IAsset.NAME, IAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new SelectBoxField(IAsset.CATEGORY, new AvailableFixedAssetCategoryListModel(companyId), IdField.NAME, IFixedAssetCategory.NAME, FieldAttributes.REQUIRED),
			new TextField(IAsset.REFERENCE, IAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(IAsset.LOCATION, IAsset.LOCATION_CONSTRAINT, FieldAttributes.OPTIONAL),

			new DateField(IAsset.ACQUISITION_DATE, FieldAttributes.OPTIONAL),
			new AcquisitionTypeField(IAsset.ACQUISITION_TYPE, FieldAttributes.REQUIRED),
			new PositiveIntegerField(IAsset.COST, FieldAttributes.OPTIONAL),
			new TextField(IAsset.PURCHASE_INVOICE, IAsset.PURCHASE_INVOICE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(IAsset.INITIAL_ACCUM_DEPRECIATION, IAsset.DEFAULT_INITIAL_ACCUM_DEPRECIATION, FieldAttributes.OPTIONAL),
		//	new IntegerSpinnerField(IAsset.INITIAL_DEP_PERIOD, IAsset.INITIAL_DEP_PERIOD_CONSTRAINT, FieldAttributes.OPTIONAL),
			new IntegerField(IAsset.INITIAL_DEPRECIATION_PERIOD, 1, FieldAttributes.OPTIONAL),

		//	new IntegerSpinnerField(IAsset.DEP_PERIOD, IAsset.DEP_PERIOD_CONSTRAINT, FieldAttributes.REQUIRED),
			new IntegerField(IAsset.DEPRECIATION_PERIOD, FieldAttributes.REQUIRED),
			new PositiveIntegerField(IAsset.RESIDUAL_VALUE, IAsset.DEFAULT_RESIDUAL_VALUE, FieldAttributes.OPTIONAL),
			new BooleanField(IAsset.CREATE_TRANSACTIONS, FieldAttributes.REQUIRED),
			new BooleanField(IAsset.OPENING, FieldAttributes.REQUIRED),
			new IntegerSpinnerField(IAsset.OPENING_YEAR, IAsset.OPENING_YEAR_CONSTRAINT, FieldAttributes.OPTIONAL),
			new MonthField(IAsset.OPENING_MONTH, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(IAsset.OPENING_ACCUM_DEPRECIATION, 0, FieldAttributes.OPTIONAL),
			new IntegerField(IAsset.OPENING_DEPRECIATION_PERIOD, 1, FieldAttributes.OPTIONAL)
				);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAssetRecord(assetId);
	}

	@Override
	public IAction<StringResult> getAddCommand(final AssetRecord record) {
		return new AddAsset(record.getName(), record.getActive(), record.getType(), record.getMinDepreciationPeriod(), record.getMaxDepreciationPeriod());
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final AssetRecord record) {
		return new UpdateAsset(record.getId(), record.getName(), record.getActive(), record.getType(), record.getMinDepreciationPeriod(), record.getMaxDepreciationPeriod());
	}
}
