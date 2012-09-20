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
package com.nabla.dc.client.ui.fixed_asset;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.nabla.dc.client.presenter.fixed_asset.AssetWizard;
import com.nabla.wapp.client.ui.WizardPage;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * @author nabla
 *
 */
public class AssetWizardGeneralPageUi extends AssetWizardBasicPageUi implements AssetWizard.IGeneralPage {

	interface Binder extends UiBinder<WizardPage, AssetWizardBasicPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	public AssetWizardGeneralPageUi(final ValuesManager model) {
		super(model, uiBinder);
	}

}
