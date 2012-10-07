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
import com.nabla.dc.client.ui.ImportSettingsWizardCompletedPageUi;
import com.nabla.dc.client.ui.ImportSettingsWizardFilePageUi;
import com.nabla.dc.client.ui.ImportSettingsWizardUi;
import com.nabla.dc.client.ui.ImportWizardErrorPageUi;
import com.nabla.wapp.client.model.ISaveWizardValuesCallback;
import com.nabla.wapp.client.mvp.AbstractWizardPresenter;
import com.nabla.wapp.client.mvp.IWizardDisplay;
import com.nabla.wapp.shared.slot.ISlot;

/**
 * @author nabla
 *
 */
public class ImportSettingsWizard extends AbstractWizardPresenter<IWizardDisplay> {

	private final ISlot						onSuccessHandler;
	private final ImportSettingsValuesManager	data = new ImportSettingsValuesManager();

	public ImportSettingsWizard(final ISlot onSuccessHandler) {
		super(new ImportSettingsWizardUi());
		this.onSuccessHandler = onSuccessHandler;
	}

	@Override
	public void bind() {
		super.bind();
		displayFilePage();
	}

	private void displayFilePage() {
		displayNextPage(new ImportSettingsWizardFilePageUi(data), new ISlot() {
			@Override
			public void invoke() {
				data.save(onFileUploaded);
			}
		});
	}

	private final ISaveWizardValuesCallback onFileUploaded = new ISaveWizardValuesCallback() {
		@Override
		public void onFailure() {
			displayErrorPage();
		}

		@Override
		public void onSuccess() {
			displayCompletedPage();
		}
	};

	private void displayErrorPage() {
		getDisplay().displayNextPage(new ImportWizardErrorPageUi(data.getRecord().getFileId()));
	}

	private void displayCompletedPage() {
		displayFinishPage(new ImportSettingsWizardCompletedPageUi(), new ISlot() {
			@Override
			public void invoke() {
				getDisplay().hide();
			}
		});
		onSuccessHandler.invoke();
	}
}
