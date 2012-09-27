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

import com.google.gwt.core.client.JsDate;
import com.nabla.dc.shared.command.fixed_asset.FetchAsset;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
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
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class AssetWizardModel extends BasicAssetModel {

	private final Integer	companyId;
	private final Integer	assetId;

	public AssetWizardModel(final Integer companyId, final Integer assetId) {
		super();

		this.companyId = companyId;
		this.assetId = assetId;
		final JsDate today = JsDate.create();
		setFields(
			new IdField(),

			new TextField(fields.name(), IAsset.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new SelectBoxField(fields.category(), new CompanyFixedAssetCategoryListModel(companyId), IdField.NAME, IFixedAssetCategory.NAME, FieldAttributes.REQUIRED),
			new TextField(fields.reference(), IAsset.REFERENCE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new TextField(fields.location(), IAsset.LOCATION_CONSTRAINT, FieldAttributes.OPTIONAL),

			new DateField(fields.acquisitionDate(), FieldAttributes.REQUIRED),
			new AcquisitionTypeField(fields.acquisitionType(), FieldAttributes.REQUIRED),
			new PositiveIntegerField(fields.cost(), FieldAttributes.REQUIRED),
			new TextField(fields.pi(), IAsset.PURCHASE_INVOICE_CONSTRAINT, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.initialAccumDep(), IAsset.DEFAULT_INITIAL_ACCUMULATED_DEPRECIATION, FieldAttributes.OPTIONAL),
			new IntegerField(fields.initialDepPeriod(), 1, FieldAttributes.OPTIONAL),

			new IntegerField(fields.depPeriod(), FieldAttributes.REQUIRED),
			new PositiveIntegerField(fields.residualValue(), IAsset.DEFAULT_RESIDUAL_VALUE, FieldAttributes.OPTIONAL),
			new BooleanField(fields.createTransactions(), FieldAttributes.REQUIRED),
			new BooleanField(fields.opening(), FieldAttributes.REQUIRED),
			new IntegerSpinnerField(fields.openingYear(), today.getFullYear(), IAsset.OPENING_YEAR_CONSTRAINT, FieldAttributes.OPTIONAL),
			new MonthField(fields.openingMonth(), today.getMonth(), FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.openingAccumDep(), FieldAttributes.OPTIONAL),
			new IntegerField(fields.openingDepPeriod(), 1, FieldAttributes.OPTIONAL)
				);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAsset(assetId);
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final AssetRecord record) {
		return record.toAddCommand(companyId);
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final AssetRecord record) {
		return record.toUpdateCommand(companyId);
	}

}
