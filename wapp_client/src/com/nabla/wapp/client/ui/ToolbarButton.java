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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HasText;
import com.nabla.wapp.client.command.ICommand;
import com.nabla.wapp.client.command.ICommandUi;
import com.nabla.wapp.client.general.Assert;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.State;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.IconClickEvent;
import com.smartgwt.client.widgets.events.IconClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;


public class ToolbarButton extends ToolStripButton implements HasText, ICommandUi {

	ICommand	command = null;
	ICommand	iconCommand = null;

	public ToolbarButton() {
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") final ClickEvent event) {
				if (command != null)
					fireCommand(command);
			}
		});
		this.addIconClickHandler(new IconClickHandler() {
			@Override
			public void onIconClick(@SuppressWarnings("unused") IconClickEvent event) {
				if (iconCommand != null)
					fireCommand(iconCommand);
				else if (command != null)
					fireCommand(command);
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
		if (this.command != null)
			this.command.removeUi(this);
		this.command = cmd;
		if (this.command != null)
			this.command.addUi(this);
	}

	public void setIconCommand(final ICommand cmd) {
		if (this.iconCommand != null)
			this.iconCommand.removeUi(this);
		this.iconCommand = cmd;
		if (this.iconCommand != null)
			this.iconCommand.addUi(this);
	}

	@Override
	public void setChecked(final boolean value) {
		Assert.state(this.getActionType() == SelectionType.CHECKBOX || this.getActionType() == SelectionType.RADIO);

		this.setState(value ? State.STATE_DOWN : State.STATE_UP);
	}

	@Override
	public void setEnabled(final boolean value) {
		this.setDisabled(!value);
	}

	private static void fireCommand(final ICommand cmd) {
		// need to defer signal to let SmartGWT update UI properly
		// otherwise problems getting dialogboxes to catch the focus for instance!
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				cmd.fire();
			}
		});
	}

	@Override
	public boolean getEnabled() {
		return !this.getDisabled();
	}
}
