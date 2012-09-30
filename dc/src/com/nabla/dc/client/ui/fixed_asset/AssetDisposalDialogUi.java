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
import com.nabla.dc.client.model.fixed_asset.AssetDisposalModel;
import com.nabla.dc.client.model.fixed_asset.AssetFields;
import com.nabla.dc.client.presenter.fixed_asset.AssetDisposalDialog;
import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * @author nabla
 *
 */
public class AssetDisposalDialogUi extends BindedModalDialog implements AssetDisposalDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, AssetDisposalDialogUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final Model		model;
	@UiField
	AssetFields		fields;
	@UiField
	Form			form;

	public AssetDisposalDialogUi(final int assetId, final ISlot1<Integer> onSuccessSlot) {
		this.model = new AssetDisposalModel(assetId);
		create(uiBinder, this);
		final FormItem proceeds = form.getItem(fields.proceeds());
		proceeds.setShowIfCondition(new FormItemIfFunction() {
            @Override
			public boolean execute(@SuppressWarnings("unused") FormItem item, @SuppressWarnings("unused") Object value, @SuppressWarnings("unused") DynamicForm f) {
            	final String s = form.getValueAsString(fields.disposalType());
            	if (s == null || s.isEmpty())
            		return false;
        		DisposalTypes disposalType = DisposalTypes.valueOf(s);
            	return disposalType == DisposalTypes.SOLD;
            }
        });
		form.getSuccessSlots(Form.Operations.UPDATE).connect(new ISlot() {
			@Override
			public void invoke() {
				onSuccessSlot.invoke(assetId);
			}
		});
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

}
