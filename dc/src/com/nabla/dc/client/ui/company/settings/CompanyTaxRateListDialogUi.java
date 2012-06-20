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
package com.nabla.dc.client.ui.company.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.company.settings.CompanyTaxRateFormModel;
import com.nabla.dc.client.model.company.settings.CompanyTaxRateListModel;
import com.nabla.dc.client.presenter.company.settings.CompanyTaxRateListDialog;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.ListGridItem;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class CompanyTaxRateListDialogUi extends BindedTopDisplay<ModalDialog> implements CompanyTaxRateListDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, CompanyTaxRateListDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final CompanyTaxRateFormModel	formModel;
	@UiField(provided=true)
	final CompanyTaxRateListModel	model;
	@UiField
	ListGridItem					list;

	public CompanyTaxRateListDialogUi(final Integer companyId) {
		this.formModel = new CompanyTaxRateFormModel(companyId);
		this.model = new CompanyTaxRateListModel(companyId);
		this.create(uiBinder, this);
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public void savePreferences() {
		list.saveViewState();
	}

}
