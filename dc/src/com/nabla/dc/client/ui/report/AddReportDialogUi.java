/**
* Copyright 2013 nabla
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
package com.nabla.dc.client.ui.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.dc.client.presenter.report.AddReportDialog;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.UploadEditBox;
import com.nabla.wapp.report.client.model.ReportRecord;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager;

public class AddReportDialogUi extends BindedModalDialog implements AddReportDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, AddReportDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField
	Form			form;
	@UiField
	UploadEditBox	file;

	public AddReportDialogUi(final ISlot1<Integer> onSuccess) {
		this.create(uiBinder, this);
		form.getSuccessSlots(Form.Operations.ADD).connect(new ISlot() {
			@Override
			public void invoke() {
				onSuccess.invoke(new ReportRecord(form.getValuesAsRecord()).getId());
			}
		});
		this.form.editNewRecord();
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public void cleanup() {
		file.cleanup();
	}

}
