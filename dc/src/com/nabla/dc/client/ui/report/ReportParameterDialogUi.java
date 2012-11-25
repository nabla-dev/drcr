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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.report.client.IReportParameterBinder;
import com.nabla.wapp.report.client.model.ReportParameterModel;
import com.nabla.wapp.report.client.presenter.ReportParameterDialog;
import com.nabla.wapp.report.client.ui.ReportParameterForm;
import com.nabla.wapp.report.shared.ReportParameterValueList;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class ReportParameterDialogUi extends BindedModalDialog implements ReportParameterDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, ReportParameterDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final Model				model;
	@UiField(provided=true)
	ReportParameterForm		form;

	public ReportParameterDialogUi(final List<IReportParameterBinder> parameters) {
		this.model = new ReportParameterModel(parameters);
		this.form = new ReportParameterForm(parameters);
		this.create(uiBinder, this);
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public ISlotManager1<ReportParameterValueList> getSubmitSlots() {
		return form.getSubmitSlotManager();
	}

}
