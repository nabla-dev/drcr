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
import com.nabla.wapp.client.model.ISaveWizardValuesCallback;
import com.nabla.wapp.client.ui.IWizardPage;
import com.nabla.wapp.client.ui.WizardPageNavigations;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;


/**
 * @author nabla64
 *
 */
public abstract class AbstractWizardPresenter<D extends IWizardDisplay> extends AbstractTopPresenter<D> {

	public class SaveValuesHandler implements ISaveWizardValuesCallback {
		@Override
		public void onFailure() {
			getDisplay().displayErroneousPage();
		}

		@Override
		public void onSuccess() {
			getDisplay().hide();
		}
	};

	protected AbstractWizardPresenter(D display) {
		super(display);
	}

	@Override
	public void unbind() {
		super.unbind();
		getDisplay().unbind();
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

		Signal1<IWizardPage> button = ui.getButton(page);
		Assert.notNull(button, "have you created a wizard page with a '" + page.toString() + "' button?");
		button.connect(nextHandler);
		getDisplay().displayNextPage(ui);
	}

}
