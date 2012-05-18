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
package com.nabla.wapp.client.mvp.binder;

import java.util.HashMap;
import java.util.Map;

import com.nabla.wapp.client.ui.IPostCreateProcessing;
import com.nabla.wapp.client.ui.IWizardPage;
import com.nabla.wapp.client.ui.WizardPageNavigations;
import com.nabla.wapp.shared.signal.Signal1;
import com.smartgwt.client.widgets.Canvas;

/**
 * @author nabla
 *
 */
public class bindedWizardPageDisplay<U extends Canvas & IPostCreateProcessing> extends BindedCanvasDisplay<U> {

	private final Map<WizardPageNavigations, Signal1<IWizardPage>>	buttons = new HashMap<WizardPageNavigations, Signal1<IWizardPage>>();

	protected bindedWizardPageDisplay(final WizardPageNavigations... buttons) {
		if (buttons == null || buttons.length == 0) {
			for (final WizardPageNavigations e : WizardPageNavigations.values())
				this.buttons.put(e, new Signal1<IWizardPage>());
		} else {
			for (final WizardPageNavigations e : buttons)
				this.buttons.put(e, new Signal1<IWizardPage>());
		}
	}

	public Signal1<IWizardPage> getButton(final WizardPageNavigations buttonType) {
		return buttons.get(buttonType);
	}

	public void unbind() {
	}

	public boolean validate() {
		return true;
	}

	public boolean hasErrors() {
		return false;
	}

}
