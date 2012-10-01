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
		Integer getBatchId();
		boolean isSuccess();
	}
	public interface IErrorPage extends IWizardPageDisplay {}
	public interface ICompletedPage extends IWizardPageDisplay {}

	private final ISlot				onSuccess;
	private final IUploadFilePage	uploadFilePage;

	public ImportAssetWizard(final IDisplay ui, final int companyId, final ISlot onSuccess) {
		super(ui);
		this.onSuccess = onSuccess;
		uploadFilePage = null/*new ImportSettingsWizardFilePageUi(onFileUploaded)*/;
	}

	public ImportAssetWizard(final int companyId, final ISlot successSlot) {
		this(new ImportAssetWizardUi(), companyId, successSlot);
	}

	@Override
	protected void onBind() {
		super.onBind();
/*
		uploadFilePage.getButton(WizardPageNavigations.NEXT).connect(new ISlot() {
			@Override
			public void invoke() {
				uploadFilePage.save();	// do actually import assets
			}
		});
		completedPage.getButton(WizardPageNavigations.FINISH).connect(new ISlot() {
			@Override
			public void invoke() {
				getDisplay().hide();
			}
		});
		getDisplay().displayNextPage(uploadFilePage);*/
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
				displayFinishPage(completedPage);
				onSuccess.invoke();
			} else {
				displayNextPage(new ImportWizardErrorPageUi(uploadFilePage.getBatchId()));
			}
		}
	};

}
