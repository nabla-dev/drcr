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
import com.nabla.dc.client.model.fixed_asset.ImportAssetUploadFileModel;
import com.nabla.dc.client.presenter.fixed_asset.ImportAssetWizard;
import com.nabla.dc.shared.model.fixed_asset.IImportAsset;
import com.nabla.wapp.client.mvp.binder.BindedWizardPageDisplay;
import com.nabla.wapp.client.ui.WizardPage;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.Form.Operations;
import com.nabla.wapp.client.ui.form.UploadEditBox;
import com.nabla.wapp.shared.command.GetFormDefaultValues;
import com.nabla.wapp.shared.slot.ISlot;

/**
 * @author nabla
 *
 */
public class ImportAssetWizardFilePageUi extends BindedWizardPageDisplay<WizardPage> implements ImportAssetWizard.IUploadFilePage {

	interface Binder extends UiBinder<WizardPage, ImportAssetWizardFilePageUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final ImportAssetUploadFileModel	model;
	@UiField
	Form								form;
	@UiField
	UploadEditBox						file;

	public ImportAssetWizardFilePageUi(final int companyId, final ISlot onSuccessHandler) {
		this.model = new ImportAssetUploadFileModel(companyId);
		this.create(uiBinder, this);
		form.editNewRecordWithDefault(new GetFormDefaultValues(IImportAsset.PREFERENCE_GROUP));
		form.getSuccessSlots(Operations.ADD).connect(onSuccessHandler);
		form.getSuccessSlots(Operations.UPDATE).connect(onSuccessHandler);
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public boolean hasErrors() {
		return form.hasErrors();
	}

	@Override
	public void save() {
		form.saveData();
	}

	@Override
	public void cleanup() {
		file.cleanup();
	}

	@Override
	public boolean isSuccess() {
		return (Boolean)form.getValue(model.fields().success());
	}

	@Override
	public int getFileId() {
		return Integer.valueOf(form.getValueAsString(model.fields().file()));
	}

}
