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
package com.nabla.wapp.client.ui;

import java.util.LinkedList;
import java.util.List;

import com.nabla.wapp.client.command.Command;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.Connection;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class WizardClientArea extends VLayout {

	private IWizardNavigationCommandSet		navigation;
	private final List<Connection<ISlot>>	slots = new LinkedList<Connection<ISlot>>();
	private final WizardPageStack			pages = new WizardPageStack();
	private boolean						enabled = true;
	private final Signal1<IWizardPage>		sigPageChanged = new Signal1<IWizardPage>();

	public WizardClientArea() {	}

	public void setNavigation(final IWizardNavigationCommandSet navigation) {
		for (final Connection<ISlot> slot : slots)
			slot.disconnect();
		slots.clear();
		this.navigation = navigation;
		if (navigation != null) {
			for (final WizardPageNavigations e : WizardPageNavigations.values()) {
				slots.add(getCommand(e).connect(new ISlot() {
					@Override
					public void invoke() {
						onNavigationClick(e);
					}
				}));
			}
		}
	}

	public void unbind() {
		pages.clear();
	}

	public IWizardNavigationCommandSet getNavigation() {
		return navigation;
	}

	public void setNavigationEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void displayPreviousPage() {
		displayPage(pages.pop(), pages.top());
	}

	public void displayNextPage(final IWizardPage nextPage) {
		Assert.argumentNotNull(nextPage);

		if (!pages.isEmpty()) {
			final Signal1<IWizardPage> previous = nextPage.getButton(WizardPageNavigations.PREVIOUS);
			if (previous != null && previous.isEmpty()) {
				// assign default handler which is to display previous page
				previous.connect(new ISlot() {
					@Override
					public void invoke() {
						displayPreviousPage();
					}
				});
			}
		}
		displayPage(pages.top(), nextPage);
		pages.push(nextPage);
	}

	public void displayErroneousPage() {
		if (!pages.isEmpty()) {
			final IWizardPage currentPage = pages.top();
			if (!currentPage.hasErrors())
				displayPage(currentPage, pages.popToFirstErroneousPage());
		}
	}

	public ISlotManager1<IWizardPage> getPageChangedSlots() {
		return sigPageChanged;
	}

	private void displayPage(final IWizardPage previousPage, final IWizardPage nextPage) {
		Assert.argumentNotNull(nextPage);

		if (previousPage != null)
			removeMember(previousPage.getImpl());
		addMember(nextPage.getImpl());
		sigPageChanged.fire(nextPage);
		setNavigationEnabled(true);
		for (final WizardPageNavigations e : WizardPageNavigations.values()) {
			final Signal1<IWizardPage> button = nextPage.getButton(e);
			getCommand(e).setVisible(button != null && !button.isEmpty());
		}
		final Signal1<IWizardPage> button = nextPage.getButton(WizardPageNavigations.FINISH);
		navigation.cancel().setVisible(button == null || button.isEmpty());
	}

	private Command getCommand(final WizardPageNavigations buttonType) {
		Assert.notNull(navigation);

		switch (buttonType) {
			case PREVIOUS:
				return navigation.previous();
			case NEXT:
				return navigation.next();
			case FINISH:
				return navigation.finish();
			default:
				return null;
		}
	}

	void onNavigationClick(final WizardPageNavigations buttonType) {
		if (enabled) {
			final IWizardPage page = pages.top();
			Assert.notNull(page);
			Assert.notNull(page.getButton(buttonType));
			Assert.state(!page.getButton(buttonType).isEmpty());
			if (buttonType == WizardPageNavigations.PREVIOUS || page.validate()) {
				page.getButton(buttonType).fire(page);
			}
		}
	}

}
