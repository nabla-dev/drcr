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

import com.nabla.dc.client.model.fixed_asset.AssetRecord;
import com.nabla.dc.client.model.fixed_asset.ViewAssetValuesManager;
import com.nabla.dc.client.ui.fixed_asset.ViewAssetDialogUi;
import com.nabla.dc.client.ui.fixed_asset.ViewAssetDisposalTabUi;
import com.nabla.dc.client.ui.fixed_asset.ViewAssetTransferTabUi;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.shared.slot.ISlot;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * @author nabla
 *
 */
public class ViewAssetDialog extends AbstractTopPresenter<ViewAssetDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		void addTab(final ITabDisplay tab);
	}

	public interface IAcquisitionTabDisplay extends ITabDisplay {}
	public interface IDisposalTabDisplay extends ITabDisplay {}
	public interface ITransferTabDisplay extends ITabDisplay {}

	private final ValuesManager	model;

	public ViewAssetDialog(final IDisplay ui, final ValuesManager model) {
		super(ui);
		this.model = model;
	}

	public ViewAssetDialog(final ValuesManager model) {
		this(new ViewAssetDialogUi(model), model);
	}

	public static void viewRecord(final Integer assetId) {
		final ViewAssetValuesManager values = new ViewAssetValuesManager(assetId);
		values.editRecord(new ISlot() {
			@Override
			public void invoke() {
				new ViewAssetDialog(values).revealDisplay();
			}
		});
	}

	@Override
	protected void onBind() {
		super.onBind();
		final AssetRecord asset = new AssetRecord(model.rememberValues());
		if (asset.isTransfer())
			getDisplay().addTab(new ViewAssetTransferTabUi(model));
		if (asset.isDisposed())
			getDisplay().addTab(new ViewAssetDisposalTabUi(model));
	}

}
