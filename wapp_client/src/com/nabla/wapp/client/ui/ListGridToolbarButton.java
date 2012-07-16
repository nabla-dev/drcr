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
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.State;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * The <code></code> object is used to
 *
 */
public class ListGridToolbarButton extends ImgButton implements HasText, ICommandUi {

	private ICommand	cmd;

	public ListGridToolbarButton() {
		setShowDown(false);
        setShowRollOver(false);
        setLayoutAlign(Alignment.CENTER);
        setHeight(16);
        setWidth(16);
        this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") ClickEvent event) {
				fireCommand(cmd);
			}
		});
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void setText(final String text) {
		setTooltip(text);
	}

	@Override
	public void setIcon(String icon) {
		setSrc(icon);
	}

	@Override
	public void setTooltip(String text) {
		setPrompt(text);
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
}
