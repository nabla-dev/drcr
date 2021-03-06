/**
* Copyright 2013 nabla
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.print.IPrintCommandSet;

public class PrintButton extends AbstractUiBinderWidgetFactory {

	interface Binder extends UiBinder<Button, PrintButton> {}
	private static Binder	uiBinder = GWT.create(Binder.class);

	@UiField(provided=true)
	IPrintCommandSet	cmd;

	public void setCommandSet(final IPrintCommandSet cmd) {
		this.cmd = cmd;
	}

	@Override
	public Widget create() {
		Assert.notNull(cmd);

		return uiBinder.createAndBindUi(this);
	}
}
