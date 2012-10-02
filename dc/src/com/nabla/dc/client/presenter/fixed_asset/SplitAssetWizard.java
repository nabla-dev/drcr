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

import com.nabla.dc.client.model.fixed_asset.SplitAssetValuesManager;
import com.nabla.dc.client.ui.fixed_asset.SplitAssetWizardCompletedPageUi;
import com.nabla.dc.client.ui.fixed_asset.SplitAssetWizardCostPageUi;
import com.nabla.dc.client.ui.fixed_asset.SplitAssetWizardGeneralPageUi;
import com.nabla.dc.client.ui.fixed_asset.SplitAssetWizardUi;
import com.nabla.dc.client.ui.fixed_asset.SplitAssetWizardWelcomePageUi;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;

/**
 * @author nabla
 *
 */
public class SplitAssetWizard extends AbstractWizardPresenter<SplitAssetWizard.IDisplay> {

	public interface IDisplay extends IWizardDisplay {}

	static public interface IWelcomePage extends IWizardPageDisplay {}
	static public interface IGeneralPage extends IWizardPageDisplay {}
	static public interface ICostPage extends IWizardPageDisplay {}
	static public interface ICompletedPage extends IWizardPageDisplay {}

	public class SaveHandler extends SaveValuesHandler {
		@Override
		public void onSuccess() {
			onRecordUpdatedSlot.invoke(assetId);
			onRecordAddedSlot.invoke(data.getRecord().getIdB());
			super.onSuccess();
		}
	};

	private final ISlot1<Integer>				onRecordUpdatedSlot;
	private final ISlot1<Integer>				onRecordAddedSlot;
	private final SplitAssetValuesManager		data;
	private final Integer						assetId;

	public SplitAssetWizard(final IDisplay ui, final SplitAssetValuesManager data, final ISlot1<Integer> onRecordUpdatedSlot, final ISlot1<Integer> onRecordAddedSlot) {
		super(ui);
		this.data = data;
		assetId = data.getRecord().getId();
		this.onRecordUpdatedSlot = onRecordUpdatedSlot;
		this.onRecordAddedSlot = onRecordAddedSlot;
	}

	public SplitAssetWizard(final SplitAssetValuesManager data, final ISlot1<Integer> onRecordUpdatedSlot, final ISlot1<Integer> onRecordAddedSlot) {
		this(new SplitAssetWizardUi(), data, onRecordUpdatedSlot, onRecordAddedSlot);
	}

	public static void editRecord(final int assetId, final ISlot1<Integer> onRecordUpdatedSlot, final ISlot1<Integer> onRecordAddedSlot) {
		final SplitAssetValuesManager values = new SplitAssetValuesManager(assetId);
		values.editRecord(new ISlot() {
			@Override
			public void invoke() {
				new SplitAssetWizard(values, onRecordUpdatedSlot, onRecordAddedSlot).revealDisplay();
			}
		});
	}

	@Override
	protected void onBind() {
		super.onBind();
		displayNextPage(new SplitAssetWizardWelcomePageUi(data.getRecord().getNameA()), new ISlot() {
			@Override
			public void invoke() {
				displayGeneralPage();
			}
		});
	}

	private void displayGeneralPage() {
		displayNextPage(new SplitAssetWizardGeneralPageUi(data), new ISlot() {
			@Override
			public void invoke() {
				displayCostPage();
			}
		});
	}

	private void displayCostPage() {
		displayNextPage(new SplitAssetWizardCostPageUi(data), new ISlot() {
			@Override
			public void invoke() {
				displayCompletedPage();
			}
		});
	}

	private void displayCompletedPage() {
		displayFinishPage(new SplitAssetWizardCompletedPageUi(), new ISlot() {
			@Override
			public void invoke() {
				data.save(new SaveHandler());
			}
		});
	}

}

