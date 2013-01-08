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

import com.google.gwt.user.client.ui.HasText;
import com.nabla.wapp.client.command.ICommand;
import com.nabla.wapp.client.command.ICommandUi;
import com.nabla.wapp.client.general.Assert;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.State;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;


public class Button extends IButton implements HasText, ICommandUi {

	ICommand	cmd = null;

	public Button() {
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
				if (cmd != null) {
				/*	Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {*/
							cmd.fire();
					/*	}
					});*/
				}
			}
		});
	}

	@Override
	public String getText() {
		return this.getTitle();
	}

	@Override
	public void setText(final String text) {
		new TitleDecoder(text).apply(this);
	}

	public void setCommand(final ICommand cmd) {
		if (this.cmd != null)
			this.cmd.removeUi(this);
		this.cmd = cmd;
		if (this.cmd != null)
			this.cmd.addUi(this);
	}

	@Override
	public void setChecked(final boolean value) {
		Assert.state(this.getActionType() == SelectionType.CHECKBOX || this.getActionType() == SelectionType.RADIO);

		this.setState(value ? State.STATE_DOWN : State.STATE_UP);
	}

	@Override
	public void setEnabled(final boolean value) {
		setDisabled(!value);
	}

	@Override
	public boolean getEnabled() {
		return !getDisabled();
	}

}
