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
package com.nabla.dc.client.presenter;

import com.nabla.dc.client.model.ImportSettingsValuesManager;
import com.nabla.dc.client.ui.ImportSettingsWizardUi;
import com.nabla.dc.client.ui.ImportWizardErrorPageUi;
import com.nabla.wapp.client.model.WizardValuesManager;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPageNavigations;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class ImportSettingsWizard extends AbstractWizardPresenter<ImportSettingsWizard.IDisplay> {

	public interface IDisplay extends IWizardDisplay {}

	public interface IUploadFilePage extends IWizardPageDisplay {
		void cleanup();
		void save();
		Integer getBatchId();
		boolean isSuccess();
	}
	public interface IErrorPage extends IWizardPageDisplay {}
	public interface ICompletedPage extends IWizardPageDisplay {}

	private final ISlot					onSuccessHandler;
	private final WizardValuesManager	data = new ImportSettingsValuesManager();

	public ImportSettingsWizard(final ISlot onSuccessHandler) {
		super(new ImportSettingsWizardUi());
		this.onSuccessHandler = onSuccessHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();

		displayNextPage(new ImportSettingsWizardFilePageUi(data), new ISlot() {
			@Override
			public void invoke() {
				data.save(callback)

			}
		});
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
		getDisplay().displayNextPage(uploadFilePage);
	}

	@Override
	protected void onUnbind() {
		uploadFilePage.cleanup();
		super.onUnbind();
	}

	private final ISlot onFileUploaded = new ISlot() {
		@Override
		public void invoke() {
			if(uploadFilePage.isSuccess()) {
				getDisplay().displayNextPage(completedPage);
				sigSuccess.fire();
			} else {
				getDisplay().displayNextPage(new ImportWizardErrorPageUi(uploadFilePage.getBatchId()));
			}
		}
	};

}
