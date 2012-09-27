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
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nabla.fixed_assets.client.model.AssetDisposalModel.Fields;
import com.nabla.fixed_assets.client.presenter.AssetDisposalDialog;
import com.nabla.fixed_assets.shared.DisposalTypes;
import com.nabla.wapp.client.model.DataParameter;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
/**
 * @author nabla
 *
 */
public class AssetDisposalDialogUi extends BindedTopDisplay<ModalDialog> implements AssetDisposalDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, AssetDisposalDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final String	recordName;
	@UiField(provided=true)
	final Model		model;
	@UiField
	Fields			fields;
	@UiField
	Form			form;

	private final Signal1<Record>	sigSuccess = new Signal1<Record>();

	@Inject
	public AssetDisposalDialogUi(@AssetDisposalDialog.IModel final Model model, @Assisted Integer assetId, @Assisted String assetName) {
		this.recordName = assetName;
		this.model = model;
		create(uiBinder, this);
		form.setDataParameter(new DataParameter<Integer>("recordId", assetId));
		final FormItem proceeds = form.getItem(fields.proceeds());
		proceeds.setShowIfCondition(new FormItemIfFunction() {
            @Override
			public boolean execute(@SuppressWarnings("unused") FormItem item, @SuppressWarnings("unused") Object value, @SuppressWarnings("unused") DynamicForm f) {
        		DisposalTypes disposalType = DisposalTypes.valueOf(form.getValueAsString(fields.disposalType()));
            	return disposalType == DisposalTypes.SOLD;
            }
        });
		form.getSuccessSlots(Form.Operations.UPDATE).connect(new ISlot() {
			@Override
			public void invoke() {
				sigSuccess.fire(form.getValuesAsRecord());
			}
		});
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public ISlotManager1<Record> getSuccessSlots() {
		return sigSuccess;
	}

}
