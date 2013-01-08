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

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.toolbar.ToolStripSpacer;


public class ToolbarSpacer extends Widget {

	private final ToolStripSpacer	impl = new ToolStripSpacer(Resource.bundle.style().DIALOG_DEFAULT_SPACING());
	private boolean					fill = false;

	public ToolbarSpacer() {}

	public ToolStripSpacer getImpl() {
		return impl;
	}

	public void setSize(int size) {
		impl.setSpace(size);
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public boolean getFill() {
		return fill;
	}
}
