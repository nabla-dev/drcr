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
package com.nabla.dc.client.ui.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.model.options.AddUserModel;
import com.nabla.dc.client.presenter.options.AddUserDialog;
import com.nabla.wapp.client.model.data.UserRecord;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;


public class AddUserDialogUi extends BindedModalDialog implements AddUserDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, AddUserDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	AddUserModel	model;
	@UiField
	Form			form;

	private final Signal1<UserRecord>	sigSuccess = new Signal1<UserRecord>();

	public AddUserDialogUi() {
		this.create(uiBinder, this);
		form.getSuccessSlots(Form.Operations.ADD).connect(new ISlot() {
			@Override
			public void invoke() {
				sigSuccess.fire(new UserRecord(form.getValuesAsRecord()));
			}
		});
		this.form.editNewRecord();
	}

	@Override
	public void clearConfirmPassword() {
		form.getStringField(model.fields().confirmPassword()).setValue("");
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public ISlotManager getFailureSlots() {
		return form.getFailureSlots(Form.Operations.ALL);
	}

	@Override
	public ISlotManager1<UserRecord> getSuccessSlots() {
		return sigSuccess;
	}

}
