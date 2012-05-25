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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.command.ICommandUi;
import com.nabla.wapp.client.command.ITRecordCommand;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * @author FNorais
 *
 */
public class ListGridToolbarMenuItem extends Widget implements HasText, ICommandUi {

	private final MenuItem			impl = new MenuItem();
	private ListGridToolbarMenu		parent;
	private ITRecordCommand			cmd;

	public ListGridToolbarMenuItem() {
		impl.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(@SuppressWarnings("unused") final MenuItemClickEvent event) {
				if (cmd != null && parent != null) {
					final JavaScriptObject jsRecord = parent.getCurrentRecord();
					if (jsRecord != null)
						cmd.execute(jsRecord);
				}
			}
		});
	}

	public MenuItem getImpl() {
		return impl;
	}

	public void setParent(final ListGridToolbarMenu parent) {
		this.parent = parent;
	}
	
	@Override
	public String getText() {
		return impl.getTitle();
	}

	@Override
	public void setText(final String text) {
		final TitleDecoder decoder = new TitleDecoder(text);
		impl.setTitle(decoder.getTitle());
		if (decoder.hasAccessKey())
			impl.setKeyTitle(decoder.getAccessKey());
	}

	public void setCommand(final ITRecordCommand cmd) {
		if (this.cmd != null)
			this.cmd.removeUi(this);
		this.cmd = cmd;
		if (this.cmd != null)
			this.cmd.addUi(this);
	}

	@Override
	public void setChecked(final boolean value) {
		impl.setChecked(value);
	}

	@Override
	public void setEnabled(final boolean value) {
		impl.setEnabled(value);
	}

	@Override
	public void setVisible(final boolean value) {
		impl.setEnabled(value);
	}

	public void setIcon(final String icon) {
		impl.setIcon(icon);
	}

	@Override
	public boolean getEnabled() {
		return impl.getEnabled();
	}
	
}