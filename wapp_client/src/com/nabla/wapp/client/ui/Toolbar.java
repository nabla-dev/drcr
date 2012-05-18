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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.client.ui.form.IFormItemSpeudoWidget;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * @author nabla
 *
 */
public class Toolbar extends ToolStrip implements HasWidgets {

	private static final Logger	logger = LoggerFactory.getLog(Toolbar.class);

	@Override
	public void add(Widget w){
		Assert.argumentNotNull(w);

		if (w instanceof AbstractUiBinderWidgetFactory)
			w = ((AbstractUiBinderWidgetFactory)w).create();
		if (w instanceof IFormItemSpeudoWidget)
			this.addFormItem(((IFormItemSpeudoWidget)w).getItem(null));
		else if (w instanceof Form) {
			final Form form = (Form)w;
			form.onCreate();
			this.addMember(form);
		} else if (w instanceof ToolbarButton)
			this.addButton((ToolbarButton)w);
		else if (w instanceof ToolbarSeparator)
			this.addMember(w);
		else if (w instanceof ToolbarSpacer) {
			final ToolbarSpacer spacer = (ToolbarSpacer)w;
			if (spacer.getFill())
				this.addFill();
			else
				this.addSpacer(spacer.getImpl());
		} else if (w instanceof ToolbarMenu)
			this.addMenuButton((ToolbarMenu)w);
		else {
			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
		}
	}

	@Override
	public void clear() {
		IHasWidgets.Helper.clear(this);
	}

	@Override
	public Iterator<Widget> iterator() {
        return IHasWidgets.Helper.iterator(this);
	}

	@Override
	public boolean remove(@SuppressWarnings("unused") final Widget w) {
		logger.log(Level.SEVERE,"removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

}
