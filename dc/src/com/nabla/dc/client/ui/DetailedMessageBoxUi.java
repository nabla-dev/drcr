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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.nabla.wapp.client.mvp.binder.BindedModalDialog;
import com.nabla.wapp.client.ui.Button;
import com.nabla.wapp.client.ui.DetailedMessageBox;
import com.nabla.wapp.client.ui.Html;
import com.nabla.wapp.client.ui.ModalDialog;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;


public class DetailedMessageBoxUi extends BindedModalDialog implements DetailedMessageBox.IDisplay {

	interface Binder extends UiBinder<ModalDialog, DetailedMessageBoxUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	static final IResource	res = Resource.bundle;
	@UiField
	Html		message;
	@UiField
	HTMLPane	detailMessage;
	@UiField
	Button		detailButton;

	public DetailedMessageBoxUi() {
		create(uiBinder, this);
		this.detailMessage.hide();
		detailButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") ClickEvent event) {
				if (detailMessage.isVisible())
					detailMessage.hide();
				else
					detailMessage.show();
			}
		});
	}

	@Override
	public ISlotManager getHideSlots() {
		return impl.getCloseSlots();
	}

	@Override
	public void setMessage(String value) {
		this.message.setHTML(value);
	}

	@Override
	public void setDetails(String value) {
		this.detailMessage.setContents(value);
	}

}
