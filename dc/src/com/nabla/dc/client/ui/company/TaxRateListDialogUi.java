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
package com.nabla.dc.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.company.TaxRateFormModel;
import com.nabla.dc.client.model.company.TaxRateListModel;
import com.nabla.dc.client.presenter.company.TaxRateListDialog;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.ListGridItem;
import com.nabla.wapp.shared.slot.ISlotManager;


public class TaxRateListDialogUi extends BindedModalDialog implements TaxRateListDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, TaxRateListDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final TaxRateFormModel	formModel;
	@UiField(provided=true)
	final TaxRateListModel	model;
	@UiField
	ListGridItem					list;

	public TaxRateListDialogUi(final Integer companyId) {
		this.formModel = new TaxRateFormModel(companyId);
		this.model = new TaxRateListModel(companyId);
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
