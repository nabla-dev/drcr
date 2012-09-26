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
import com.nabla.dc.client.ui.fixed_asset.ViewAssetDisposalTabUi;
import com.nabla.dc.client.ui.fixed_asset.ViewAssetTransferTabUi;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.ITopDisplay;

/**
 * @author nabla
 *
 */
public class ViewAssetDialog extends AbstractTopPresenter<ViewAssetDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		void addTab(final ITabDisplay tab);
	}

	public interface IDisposalTabDisplay extends ITabDisplay {}
	public interface ITransferTabDisplay extends ITabDisplay {}

	private final AssetRecord	asset;

	public ViewAssetDialog(final IDisplay ui, final AssetRecord asset) {
		super(ui);
		this.asset = asset;
	}

	public ViewAssetDialog(final AssetRecord asset) {
		super(new ViewAssetUi());
		this.asset = asset;
	}

	@Override
	protected void onBind() {
		super.onBind();
		if (asset.isTransfer())
			getDisplay().addTab(new ViewAssetTransferTabUi(asset.getId()));
		if (asset.isDisposed())
			getDisplay().addTab(new ViewAssetDisposalTabUi(asset.getId()));
	}

}
