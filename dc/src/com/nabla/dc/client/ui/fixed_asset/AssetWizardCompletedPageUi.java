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
import com.google.gwt.uibinder.client.UiTemplate;
import com.nabla.dc.client.presenter.fixed_asset.AssetWizard;
import com.nabla.wapp.client.mvp.binder.BindedStaticWizardPageDisplay;
import com.nabla.wapp.client.ui.Html;

/**
 * @author nabla
 *
 */
public class AssetWizardCompletedPageUi extends BindedStaticWizardPageDisplay implements AssetWizard.ICompletedPage {

	@UiTemplate("NewAssetWizardCompletedPageUi.ui.xml")
	interface NewBinder extends UiBinder<Html, AssetWizardCompletedPageUi> {}
	private static final NewBinder	newBinder = GWT.create(NewBinder.class);

	@UiTemplate("EditAssetWizardCompletedPageUi.ui.xml")
	interface EditBinder extends UiBinder<Html, AssetWizardCompletedPageUi> {}
	private static final EditBinder	editBinder = GWT.create(EditBinder.class);

	public AssetWizardCompletedPageUi(boolean isNewRecord) {
		create(isNewRecord ? newBinder : editBinder, this);
	}

}
