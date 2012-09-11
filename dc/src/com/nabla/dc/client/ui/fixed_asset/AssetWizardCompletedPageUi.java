/**
* Copyright 2010 nabla
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
package com.nabla.fixed_assets.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nabla.fixed_assets.client.presenter.AssetWizard;
import com.nabla.wapp.client.ui.Html;

/**
 * @author nabla
 *
 */
public class AssetWizardCompletedPageUi extends StaticWizardPageUi implements AssetWizard.ICompletedPage {

	@UiTemplate("NewAssetWizardCompletedPageUi.ui.xml")
	interface NewBinder extends UiBinder<Html, StaticWizardPageUi> {}
	private static final NewBinder	newBinder = GWT.create(NewBinder.class);

	@UiTemplate("EditAssetWizardCompletedPageUi.ui.xml")
	interface EditBinder extends UiBinder<Html, StaticWizardPageUi> {}
	private static final EditBinder	editBinder = GWT.create(EditBinder.class);

	@Inject
	public AssetWizardCompletedPageUi(@Assisted boolean isNewRecord) {
		super(isNewRecord ? newBinder : editBinder);
	}

}
