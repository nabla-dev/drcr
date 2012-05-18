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
package com.nabla.dc.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.model.LoginModel;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.mvp.binder.BindedTopDisplay;
import com.nabla.wapp.client.server.ILoginDialogUi;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.shared.general.IHasValue;
import com.nabla.wapp.shared.model.IUser;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class ReLoginDialogUi extends BindedTopDisplay<ModalDialog> implements ILoginDialogUi {

	interface Binder extends UiBinder<ModalDialog, ReLoginDialogUi> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	static final IResource	res = Resource.bundle;
	@UiField(provided=true)
	static final Model		model = new LoginModel();
	@UiField
	Form					form;

	public ReLoginDialogUi() {
		this.create(uiBinder, this);
	}

	@Override
	public ISlotManager getSubmitSlots() {
		return form.getSubmitSlots();
	}

	@Override
	public IHasValue<String> getPassword() {
		return form.getStringField(IUser.PASSWORD);
	}

	@Override
	public IHasValue<String> getUserName() {
		return form.getStringField(IUser.NAME);
	}

	@Override
	public <E extends Enum<E>> void showError(final E error) {
		final String msg = Application.getInstance().getLocalizedError(error);
		final Map<String, String> errors = new HashMap<String, String>();
		errors.put(IUser.NAME, msg);
		errors.put(IUser.PASSWORD, msg);
		form.setErrors(errors, true);
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public void setSaving(final boolean saving) {
		form.setSaving(saving);
	}


}
