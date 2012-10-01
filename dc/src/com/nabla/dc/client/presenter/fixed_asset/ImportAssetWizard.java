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

import com.nabla.dc.client.ui.ImportWizardErrorPageUi;
import com.nabla.dc.client.ui.fixed_asset.ImportAssetWizardCompletedPageUi;
import com.nabla.dc.client.ui.fixed_asset.ImportAssetWizardFilePageUi;
import com.nabla.dc.client.ui.fixed_asset.ImportAssetWizardUi;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.shared.slot.ISlot;

/**
 * @author nabla
 *
 */
public class ImportAssetWizard extends AbstractWizardPresenter<ImportAssetWizard.IDisplay> {

	public interface IDisplay extends IWizardDisplay {}

	public interface IUploadFilePage extends IWizardPageDisplay {
		void cleanup();
		void save();
		int getFileId();
		boolean isSuccess();
	}
	public interface IErrorPage extends IWizardPageDisplay {}
	public interface ICompletedPage extends IWizardPageDisplay {}

	private final ISlot			onSuccessHandler;
	private final IUploadFilePage	uploadFilePage;

	public ImportAssetWizard(final IDisplay ui, final int companyId, final ISlot onSuccessHandler) {
		super(ui);
		this.onSuccessHandler = onSuccessHandler;
		uploadFilePage = new ImportAssetWizardFilePageUi(companyId, onFileUploaded);
	}

	public ImportAssetWizard(final int companyId, final ISlot successSlot) {
		this(new ImportAssetWizardUi(), companyId, successSlot);
	}

	@Override
	protected void onBind() {
		super.onBind();
		displayNextPage(uploadFilePage, new ISlot() {
			@Override
			public void invoke() {
				uploadFilePage.save();	// do actually import assets
			}
		});
	}

	@Override
	protected void onUnbind() {
		uploadFilePage.cleanup();
		super.onUnbind();
	}

	private final ISlot onFileUploaded = new ISlot() {
		@Override
		public void invoke() {
			if (uploadFilePage.isSuccess()) {
				displayCompletedPage();
				onSuccessHandler.invoke();
			} else
				displayErrorPage();
		}
	};

	private void displayErrorPage() {
		getDisplay().displayNextPage(new ImportWizardErrorPageUi(uploadFilePage.getFileId()));
	}

	private void displayCompletedPage() {
		displayFinishPage(new ImportAssetWizardCompletedPageUi(), new ISlot() {
			@Override
			public void invoke() {
				getDisplay().hide();
			}
		});
	}

}
