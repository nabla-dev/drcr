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
import com.nabla.dc.client.presenter.MainWindow;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.mvp.ICanvasDisplay;
import com.nabla.wapp.client.mvp.binder.BindedDisplay;
import com.nabla.wapp.client.ui.VLayout;
import com.smartgwt.client.widgets.Canvas;


/**
 * @author nabla
 *
 */
public class MainWindowUi extends BindedDisplay<VLayout> implements MainWindow.IDisplay {

	interface Binder extends UiBinder<VLayout, MainWindowUi> {}
	private static final Binder	uiBinder = GWT.create(Binder.class);

	@UiField VLayout	clientArea;

	private Canvas		client = null;

	public MainWindowUi() {
		this.create(uiBinder, this);
	}

	@Override
	public void show() {
		impl.show();
	}

	@Override
	public void setClientArea(final ICanvasDisplay client) {
		if (this.client != null)
			clientArea.removeMember(this.client);
		if (client != null) {
			this.client = client.getImpl();
			Assert.notNull(this.client);
			clientArea.add(this.client);
		} else
			this.client = null;
	}

}
