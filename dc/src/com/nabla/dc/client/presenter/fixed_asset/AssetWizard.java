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
import com.google.inject.Inject;
import com.nabla.dc.shared.command.fixed_asset.FetchAssetListRecord;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.client.general.AbstractAsyncCallback;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.client.ui.IWizardPage;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class AssetWizard extends AbstractWizardPresenter<AssetWizard.IDisplay> {

	public interface IDisplay extends IWizardDisplay {}
	public interface IWelcomePage extends IWizardPageDisplay {}
	public interface IGeneralPage extends IWizardPageDisplay {
		Integer getAssetCategoryId();
	}
	public interface IAcquisitionPage extends IWizardPageDisplay {
		boolean canCreateTransaction();
	}
	public interface IDepreciationPage extends IWizardPageDisplay {}
	public interface ICompletedPage extends IWizardPageDisplay {}

	private static final Logger					logger = LoggerFactory.getLog(AssetWizard.class);
	private final WizardData					data;
	private final ISlot1<Record>				onSuccessHandler;

	// TODO:
	// I would like to have Provider<IWelcomePage> but I get a compiler error!
	@Inject private IWelcomePage				welcomePage;
	@Inject private IGeneralPageFactory			generalPageFactory;
	@Inject private IAcquisitionPageFactory		acquisitionPageFactory;
	@Inject private IDepreciationPageFactory	depreciationPageFactory;
	@Inject private ICompletedPageFactory		completedPageFactory;

	@Inject private IDispatchAsync				server;

	@Inject
	public AssetWizard(IDisplay ui, @Assisted WizardData data, @Assisted ISlot1<Record> onSuccessHandler) {
		super(ui);
		this.data = data;
		this.onSuccessHandler = onSuccessHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();
		if (data.isNewRecord()) {
			displayNextPage(welcomePage, new ISlot() {
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
		displayNextPage(generalPageFactory.get(data), new ISlot1<IWizardPage>() {
			@Override
			public void invoke(final IWizardPage page) {
				server.execute(new GetAssetCategoryDepreciationPeriodRange(((IGeneralPage)page).getAssetCategoryId()), new AsyncCallback<AssetCategoryDepreciationPeriodRange>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.WARNING, "failed to get depreciation range. use default range", caught);
						displayAcquisitionPage(new AssetCategoryDepreciationPeriodRange(IAsset.DEP_PERIOD_CONSTRAINT.getMinValue(), IAsset.DEP_PERIOD_CONSTRAINT.getMaxValue()));
					}

					@Override
					public void onSuccess(AssetCategoryDepreciationPeriodRange range) {
						logger.fine("restricting depreciation range to [" + range.getMin() + ", " + range.getMax() + "]");
						displayAcquisitionPage(range);
					}
				});
			}
		});
	}

	private void displayAcquisitionPage(final AssetCategoryDepreciationPeriodRange depreciationPeriodRange) {
		displayNextPage(acquisitionPageFactory.get(data, depreciationPeriodRange.getMax()), new ISlot1<IWizardPage>() {
			@Override
			public void invoke(final IWizardPage page) {
				displayDepreciationPage(depreciationPeriodRange, ((IAcquisitionPage)page).canCreateTransaction());
			}
		});
	}

	private void displayDepreciationPage(AssetCategoryDepreciationPeriodRange depreciationPeriodRange, boolean canCreateTransaction) {
		displayNextPage(depreciationPageFactory.get(data, depreciationPeriodRange, canCreateTransaction), new ISlot() {
			@Override
			public void invoke() {
				displayCompletedPage();
			}
		});
	}

	private void displayCompletedPage() {
		displayFinishPage(completedPageFactory.get(data.isNewRecord()), new ISlot() {
			@Override
			public void invoke() {
				data.saveData(onAssetSaved);
			}
		});
	}

	@SuppressWarnings("hiding")
	public final DSCallback onAssetSaved = new DSCallback() {
		@Override
		public void execute(DSResponse response, Object rawData, DSRequest request) {
			if (response.getStatus() == DSResponse.STATUS_SUCCESS) {
				server.execute(new FetchAssetListRecord((Integer)data.getValue(IdField.NAME)), new AbstractAsyncCallback<StringResult>() {
					@Override
					public void onSuccess(StringResult result) {
						logger.fine("asset = " + result.get());
						Record[] records = data.getModel().recordsFromXml(result.get());
						onSuccessHandler.invoke(records[0]);
					}
				});
			}
			onSave.execute(response, rawData, request);
		}
	};

}

