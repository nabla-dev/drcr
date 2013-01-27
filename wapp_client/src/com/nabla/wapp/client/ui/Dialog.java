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
package com.nabla.wapp.client.ui;

import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.Form.Operations;
import com.nabla.wapp.shared.signal.Signal;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;


public class Dialog extends Window implements IHasWidgets, IPostCreateProcessing {

	private static final Logger	log = LoggerFactory.getLog(Dialog.class);
	private static int			margin = Resource.bundle.style().DIALOG_DEFAULT_MARGIN();
	private static int			spacing = Resource.bundle.style().DIALOG_DEFAULT_SPACING();
	private final Signal			sigClose = new Signal();
	private boolean				autoClose = false;
	private final VLayout			layout = new VLayout();

	public Dialog() {
		this.setShowMaximizeButton(false);
		this.setShowMinimizeButton(false);
		this.setShowCloseButton(false);	// will be set to true if CancelButton is found in children
		this.setDismissOnEscape(false);	// will be set to true if CancelButton is found in children
		this.setAutoSize(true);
		layout.setMargin(2);
		layout.setLayoutMargin(margin);
		layout.setMembersMargin(0/*spacing*/);
		layout.setBorder("solid medium lightblue");
		layout.setShowEdges(true);
		layout.setWidth("100%");
		layout.setHeight("100%");
		addItem(layout);
	}

	static public void setDefaultMargin(int value) {
		margin = value;
	}

	static public void setDefaultSpacing(int value) {
		spacing = value;
	}

	@Override
	public void add(final Widget w){
		if (w instanceof Form) {
			final Form form = (Form)w;
			form.getSuccessSlots(Operations.ADD).connect(onSuccess);
			form.getSuccessSlots(Operations.UPDATE).connect(onSuccess);
		}
		layout.add(w);
	}

	@Override
	public void hide() {
		super.hide();
		sigClose.fire();
	}

	@Override
	public void clear() {
//		IHasWidgets.Helper.clear(this);
	}

	@Override
	public Iterator<Widget> iterator() {
        return null/*layout.iterator()*/;
	}

	@Override
	public boolean remove(@SuppressWarnings("unused") final Widget w) {
		return false/*layout.remove(w)*/;
	}

	@Override
	public <T> T findChild(final Class<T> type, final boolean recursive) {
		return layout.findChild(type, recursive);
	}

	public ISlotManager getCloseSlots() {
		return sigClose;
	}

	public void setCaption(final String caption) {
		setTitle(caption);
	}

	public void setModal(final boolean isModal) {
		setIsModal(isModal);
	}

	public void setIcon(final String imgSrc) {
		log.fine("setting dialog icon to '" + imgSrc + "'");
		setHeaderIcon(imgSrc);
	}

	public void setAutoClose(boolean value) {
		autoClose = value;
	}

	@Override
	public void onCreate() {
		final CancelButton button = findChild(CancelButton.class, true);
		if (button != null) {
			log.info("found CancelButton. installing handlers to close Dialog");
			this.setShowCloseButton(true);
			this.setDismissOnEscape(true);
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
					Dialog.this.hide();
				}
			});
			this.addCloseClickHandler(new CloseClickHandler() {
				@Override
				public void onCloseClick(@SuppressWarnings("unused") CloseClickEvent event) {
					Dialog.this.hide();
				}
			});
		} else {
			log.warning("no CancelButton found");
		}
	}

	private final ISlot onSuccess = new ISlot() {
		@Override
		public void invoke() {
			if (autoClose) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						hide();
					}
				});
			}
		}
	};

}