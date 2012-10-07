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

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.command.fixed_asset.AddAsset;
import com.nabla.dc.shared.command.fixed_asset.UpdateAsset;
import com.nabla.dc.shared.command.fixed_asset.UpdateAssetDisposal;
import com.nabla.dc.shared.command.fixed_asset.UpdateAssetField;
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.JSHelper;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.IWizardRecord;
import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.nabla.wapp.shared.general.Nullable;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class AssetRecord extends BasicListGridRecord implements IAsset, IWizardRecord {

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
		return getAttributeAsString(NAME);
	}

	public String getCategory() {
		return getAttributeAsString(CATEGORY);
	}

	public Integer getCategoryId() {
		return Integer.valueOf(getCategory());
	}

	public String getReference() {
		return getAttributeAsString(REFERENCE);
	}

	public String getLocation() {
		return getAttributeAsString(LOCATION);
	}

	public Date getAcquisitionDate() {
		return getAttributeAsDate(ACQUISITION_DATE);
	}

	public AcquisitionTypes getAcquisitionType() {
		return AcquisitionTypes.valueOf(getAttributeAsString(ACQUISITION_TYPE));
	}

	public boolean isTransfer() {
		return getAcquisitionType() == AcquisitionTypes.TRANSFER;
	}

	public Integer getCost() {
		return getAttributeAsInt(COST);
	}

	public String getPurchaseInvoice() {
		return getAttributeAsString(PURCHASE_INVOICE);
	}

	public Integer getInitialAccumulatedDepreciation() {
		return getAttributeAsInt(INITIAL_ACCUMULATED_DEPRECIATION);
	}

	public Integer getInitialDepreciationPeriod() {
		return getAttributeAsInt(INITIAL_DEPRECIATION_PERIOD);
	}

	public Integer getDepreciationPeriod() {
		return getAttributeAsInt(DEPRECIATION_PERIOD);
	}

	public Integer getResidualValue() {
		return getAttributeAsInt(RESIDUAL_VALUE);
	}

	public boolean getCreateTransactions() {
		return getAttributeAsBoolean(CREATE_TRANSACTIONS);
	}

	public boolean isOpening() {
		return getAttributeAsBoolean(OPENING);
	}

	public Integer getOpeningYear() {
		return getAttributeAsInt(OPENING_YEAR);
	}

	public Integer getOpeningMonth() {
		return getAttributeAsInt(OPENING_MONTH);
	}

	public Integer getOpeningAccumulatedDepreciation() {
		return getAttributeAsInt(OPENING_ACCUMULATED_DEPRECIATION);
	}

	public Integer getOpeningDepreciationPeriod() {
		return getAttributeAsInt(OPENING_DEPRECIATION_PERIOD);
	}

	public Boolean isDisposed() {
		String date = getAttributeAsString(DISPOSAL_DATE);
		return date != null && !date.isEmpty();
	}

	public Date getDisposalDate() {
		return getAttributeAsDate(DISPOSAL_DATE);
	}

	public DisposalTypes getDisposalType() {
		return DisposalTypes.valueOf(getAttributeAsString(DISPOSAL_TYPE));
	}

	public @Nullable Integer getProceeds() {
		return JSHelper.isAttribute(this.getJsObj(), PROCEEDS) ? getAttributeAsInt(PROCEEDS) : null;
	}

	public AddAsset toAddCommand(final Integer companyId) {
		final AddAsset cmd = new AddAsset(companyId, getName(), getCategoryId(), getReference(), getLocation(),
				getAcquisitionDate(), getAcquisitionType(), getCost(), getPurchaseInvoice(),
				getDepreciationPeriod(), getResidualValue(),
				getCreateTransactions());
		if (isTransfer())
			cmd.setInitialDepreciation(getInitialAccumulatedDepreciation(), getInitialDepreciationPeriod());
		if (isOpening())
			cmd.setOpeningDepreciation(getOpeningYear(), getOpeningMonth(), getOpeningAccumulatedDepreciation(), getOpeningDepreciationPeriod());
		return cmd;
	}

	public UpdateAsset toUpdateCommand(final Integer companyId) {
		final UpdateAsset cmd = new UpdateAsset(getId(), companyId, getName(), getCategoryId(), getReference(), getLocation(),
				getAcquisitionDate(), getAcquisitionType(), getCost(), getPurchaseInvoice(),
				getDepreciationPeriod(), getResidualValue(),
				getCreateTransactions());
		if (isTransfer())
			cmd.setInitialDepreciation(getInitialAccumulatedDepreciation(), getInitialDepreciationPeriod());
		if (isOpening())
			cmd.setOpeningDepreciation(getOpeningYear(), getOpeningMonth(), getOpeningAccumulatedDepreciation(), getOpeningDepreciationPeriod());
		return cmd;
	}

	public UpdateAssetField toUpdateFieldCommand() {
		return new UpdateAssetField(getId(), getName(), getReference(), getLocation(), getPurchaseInvoice(), getDepreciationPeriod());
	}

	public UpdateAssetDisposal toDisposalCommand() {
		return new UpdateAssetDisposal(getId(), getDisposalDate(), getDisposalType(), getProceeds());
	}

	@Override
	public boolean getSuccess() {
		return true;
	}
}
