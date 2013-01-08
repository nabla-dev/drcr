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
import com.google.gwt.uibinder.client.UiField;
import com.nabla.wapp.client.mvp.IWizardPageDisplay;
import com.nabla.wapp.client.mvp.binder.BindedBasicWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPage;
import com.nabla.wapp.client.ui.form.UploadEditBox;
import com.smartgwt.client.widgets.form.ValuesManager;


public class ImportAssetsWizardFilePageUi extends BindedBasicWizardPageDisplay implements IWizardPageDisplay {

	interface Binder extends UiBinder<WizardPage, ImportAssetsWizardFilePageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	UploadEditBox	file;

	public ImportAssetsWizardFilePageUi(final ValuesManager model) {
		super(model);
		this.create(uiBinder, this);
	}

	@Override
	public void unbind() {
		file.cleanup();
		super.unbind();
	}
}
