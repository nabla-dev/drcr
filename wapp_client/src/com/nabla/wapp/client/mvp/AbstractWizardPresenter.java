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
package com.nabla.wapp.client.mvp;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.ui.IWizardPage;
import com.nabla.wapp.client.ui.WizardPageNavigations;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;


/**
 * @author nabla64
 *
 */
public abstract class AbstractWizardPresenter<D extends IWizardDisplay> extends AbstractTopPresenter<D> {

	protected AbstractWizardPresenter(D display) {
		super(display);
	}

	public void displayNextPage(final IWizardPageDisplay ui, final ISlot1<IWizardPage> nextHandler) {
		displayNextPage(ui, nextHandler, WizardPageNavigations.NEXT);
	}

	public void displayFinishPage(final IWizardPageDisplay ui, final ISlot1<IWizardPage> nextHandler) {
		displayNextPage(ui, nextHandler, WizardPageNavigations.FINISH);
	}

	public void displayNextPage(final IWizardPageDisplay ui, final ISlot1<IWizardPage> nextHandler, final WizardPageNavigations page) {
		Assert.argumentNotNull(ui);

		ui.getButton(page).connect(nextHandler);
		getDisplay().displayNextPage(ui);
	}

	public void displayNextPage(final IWizardPageDisplay ui, final ISlot nextHandler) {
		displayNextPage(ui, nextHandler, WizardPageNavigations.NEXT);
	}

	public void displayFinishPage(final IWizardPageDisplay ui, final ISlot nextHandler) {
		displayNextPage(ui, nextHandler, WizardPageNavigations.FINISH);
	}

	public void displayNextPage(final IWizardPageDisplay ui, final ISlot nextHandler, final WizardPageNavigations page) {
		Assert.argumentNotNull(ui);

		ui.getButton(page).connect(nextHandler);
		getDisplay().displayNextPage(ui);
	}

	public final DSCallback onSave = new DSCallback() {
		@Override
		public void execute(DSResponse response, @SuppressWarnings("unused") Object rawData, @SuppressWarnings("unused") DSRequest request) {
			if (response.getStatus() == DSResponse.STATUS_SUCCESS)
				getDisplay().hide();
			else
				getDisplay().displayErroneousPage();
		}
	};

}
