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
import com.nabla.wapp.client.ui.IHasWidgets.Helper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * The <code></code> object is used to
 *
 */
public class SectionPanel extends UiBinderSpeudoWidgetList implements IPostCreateProcessing {

	private final SectionStackSection	impl = new SectionStackSection();

	public SectionStackSection getImpl() {
		return impl;
	}

	@Override
	public void add(Widget w){
		Helper.onCreate(w);
		if (w instanceof Toolbar) {
			Toolbar tb = (Toolbar)w;
			tb.setAutoWidth();
			tb.setAutoHeight();
			impl.setControls(tb);
		} else if (w instanceof Canvas)
			impl.setItems((Canvas)w);
		else
			super.add(w);
	}

	@Override
	public void setTitle(String title) {
		impl.setTitle(title);
	}

	@Override
	public void onCreate() {

	}

	public void setExpanded(Boolean value) {
		impl.setExpanded(value);
	}

}
