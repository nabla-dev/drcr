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
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nabla.fixed_assets.client.model.AssetCategoryRecord;
import com.nabla.fixed_assets.client.presenter.AssetWizard;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.ui.WizardPage;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * @author nabla
 *
 */
public class AssetWizardGeneralPageUi extends AssetWizardBasicPageUi implements AssetWizard.IGeneralPage {

	interface Binder extends UiBinder<WizardPage, AssetWizardBasicPageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@Inject
	public AssetWizardGeneralPageUi(@Assisted final ValuesManager model) {
		super(model, uiBinder);
	}

	@Override
	public Integer getAssetCategoryId() {
		final SelectItem categories = (SelectItem)form.getField(IAsset.CATEGORY);
		Assert.notNull(categories);
		final AssetCategoryRecord category = new AssetCategoryRecord(categories.getSelectedRecord());
		return category.getId();
	}

}
