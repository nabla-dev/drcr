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
package com.nabla.dc.client.presenter.fixed_asset;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.dc.client.model.fixed_asset.AssetWizardValuesManager;
import com.nabla.dc.client.ui.fixed_asset.AssetWizardAcquisitionPageUi;
import com.nabla.dc.client.ui.fixed_asset.AssetWizardCompletedPageUi;
import com.nabla.dc.client.ui.fixed_asset.AssetWizardDepreciationPageUi;
import com.nabla.dc.client.ui.fixed_asset.AssetWizardGeneralPageUi;
import com.nabla.dc.client.ui.fixed_asset.AssetWizardUi;
import com.nabla.dc.client.ui.fixed_asset.AssetWizardWelcomePageUi;
import com.nabla.dc.shared.command.fixed_asset.GetFixedAssetCategoryDepreciationPeriodRange;
import com.nabla.dc.shared.model.fixed_asset.DepreciationPeriodRange;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;

/**
 * @author nabla
 *
 */
public class AssetWizard extends AbstractWizardPresenter<AssetWizard.IDisplay> {

	public interface IDisplay extends IWizardDisplay {}
	public interface IWelcomePage extends IWizardPageDisplay {}
	public interface IGeneralPage extends IWizardPageDisplay {}
	public interface IAcquisitionPage extends IWizardPageDisplay {}
	public interface IDepreciationPage extends IWizardPageDisplay {}
	public interface ICompletedPage extends IWizardPageDisplay {}

	private static final Logger				logger = LoggerFactory.getLog(AssetWizard.class);
	private final AssetWizardValuesManager		data;
	private final ISlot1<Integer>				onSuccessHandler;

	protected AssetWizard(final IDisplay ui, final AssetWizardValuesManager data, final ISlot1<Integer> onSuccessHandler) {
		super(ui);
		this.data = data;
		this.onSuccessHandler = onSuccessHandler;
	}

	protected AssetWizard(final AssetWizardValuesManager values, final ISlot1<Integer> onSuccessHandler) {
		this(new AssetWizardUi(), values, onSuccessHandler);
	}

	public static void editNewRecord(final Integer companyId, final ISlot1<Integer> onSuccessHandler) {
		final AssetWizardValuesManager values = new AssetWizardValuesManager(companyId);
		values.editNewRecord(companyId, IAsset.PREFERENCE_GROUP, new ISlot() {
			@Override
			public void invoke() {
				new AssetWizard(values, onSuccessHandler).revealDisplay();
			}
		});
	}

	public static void editRecord(final Integer companyId, final Integer assetId, final ISlot1<Integer> onSuccessHandler) {
		final AssetWizardValuesManager values = new AssetWizardValuesManager(companyId, assetId);
		values.editRecord(new ISlot() {
			@Override
			public void invoke() {
				new AssetWizard(values, onSuccessHandler).revealDisplay();
			}
		});
	}

	@Override
	protected void onBind() {
		super.onBind();
		if (data.isNewRecord()) {
			displayNextPage(new AssetWizardWelcomePageUi(), new ISlot() {
				@Override
				public void invoke() {
					displayGeneralPage();
				}
			});
		} else {
			// if edit asset, skip welcome page
			displayGeneralPage();
		}
	}

	private void displayGeneralPage() {
		displayNextPage(new AssetWizardGeneralPageUi(data), new ISlot() {
			@Override
			public void invoke() {
				Integer fixedAssetCategoryId = Integer.valueOf(data.getValueAsString(IAsset.CATEGORY));
				Application.getInstance().getDispatcher().execute(new GetFixedAssetCategoryDepreciationPeriodRange(fixedAssetCategoryId), new AsyncCallback<DepreciationPeriodRange>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.WARNING, "failed to get depreciation range. use default range", caught);
						displayAcquisitionPage(new DepreciationPeriodRange(IAsset.DEPRECIATION_PERIOD_CONSTRAINT.getMinValue(), IAsset.DEPRECIATION_PERIOD_CONSTRAINT.getMaxValue()));
					}

					@Override
					public void onSuccess(DepreciationPeriodRange range) {
						logger.fine("restricting depreciation range to [" + range.getMin() + ", " + range.getMax() + "]");
						displayAcquisitionPage(range);
					}
				});
			}
		});
	}

	private void displayAcquisitionPage(final DepreciationPeriodRange depreciationPeriodRange) {
		displayNextPage(new AssetWizardAcquisitionPageUi(data, depreciationPeriodRange.getMax()), new ISlot() {
			@Override
			public void invoke() {
				displayDepreciationPage(depreciationPeriodRange);
			}
		});
	}

	private void displayDepreciationPage(final DepreciationPeriodRange depreciationPeriodRange) {
		displayNextPage(new AssetWizardDepreciationPageUi(data, depreciationPeriodRange), new ISlot() {
			@Override
			public void invoke() {
				displayCompletedPage();
			}
		});
	}

	private void displayCompletedPage() {
		displayFinishPage(new AssetWizardCompletedPageUi(data.isNewRecord()), new ISlot() {
			@Override
			public void invoke() {
				data.saveData(onAssetSaved);
			}
		});
	}

	public final DSCallback onAssetSaved = new DSCallback() {
		@Override
		public void execute(DSResponse response, Object rawData, DSRequest request) {
			if (response.getStatus() == DSResponse.STATUS_SUCCESS)
				onSuccessHandler.invoke((Integer)data.getValue(IdField.NAME));
			onSave.execute(response, rawData, request);
		}
	};

}

