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
package com.nabla.dc.client.ui;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.Html;
import com.nabla.wapp.client.ui.IWizardPage;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.WizardClientArea;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.smartgwt.client.widgets.Canvas;

/**
 * @author nabla
 *
 */
public class WizardUi extends BindedTopDisplay<ModalDialog> {

	@UiField(provided=true)
	static public final IResource	res = Resource.bundle;
	@UiField
	public WizardClientArea			client;
	@UiField
	public Html						pageTitle;

	public WizardUi(final UiBinder<ModalDialog, WizardUi> uiBinder) {
		this.create(uiBinder, this);
		client.getPageChangedSlots().connect(new ISlot1<IWizardPage>() {
			@Override
			public void invoke(final IWizardPage page) {
				final Canvas ctrl = page.getImpl();
				pageTitle.setHTML(ctrl.getTitle());
				pageTitle.markForRedraw();
			}
		});
	}

	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	public void displayNextPage(final IWizardPage page) {
		client.displayNextPage(page);
	}

	public void displayErroneousPage() {
		client.displayErroneousPage();
	}

	public void setNavigationEnabled(final boolean enabled) {
		client.setNavigationEnabled(enabled);
	}
}