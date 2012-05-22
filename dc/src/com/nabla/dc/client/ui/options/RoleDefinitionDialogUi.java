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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.dc.client.model.options.RoleDefinitionModel;
import com.nabla.dc.client.presenter.options.RoleDefinitionDialog;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.TreeGrid;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class RoleDefinitionDialogUi extends BindedTopDisplay<ModalDialog> implements RoleDefinitionDialog.IDisplay {

	interface Binder extends UiBinder<ModalDialog, RoleDefinitionDialogUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	final Model		model;
	@UiField
	Form			form;
	@UiField(provided=true)
	final Model		treeModel;
	@UiField(provided=true)
	TreeGrid		tree = new RoleTreeGrid();

	public RoleDefinitionDialogUi(final RoleDefinitionModel formModel) {
		this.model = formModel;
		this.treeModel = formModel.getTreeModel();
		this.create(uiBinder, this);
		formModel.getDispatcher().execute(formModel.getRoleName(), new AsyncCallback<StringResult>() {
			@Override
			public void onSuccess(final StringResult result) {
				if (result != null) {
					form.setValue("recordName", result.get());
				} else
					impl.hide();
			}

			@Override
			public void onFailure(@SuppressWarnings("unused") final Throwable __) {
				impl.hide();
			}
		});		
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

}
