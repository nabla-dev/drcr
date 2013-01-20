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
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 * The <code></code> object is used to
 *
 */
public class ListGridToolbarMenu extends ToolStripMenuButton implements HasWidgets, IPostCreateProcessing {

	private static final Logger	logger = LoggerFactory.getLog(ListGridToolbarMenu.class);
	private final Menu			menu = new Menu();

	public ListGridToolbarMenu() {
		menu.setHeight(16);
		menu.setWidth(16);
		super.setTitle("<span/>");
        this.setMenu(menu);
	}

	@Override
	public void add(final Widget w){
		Assert.argumentNotNull(w);

		if (w instanceof Menu) {
			final com.smartgwt.client.widgets.menu.MenuItem proxy = new com.smartgwt.client.widgets.menu.MenuItem(w.getTitle());
			proxy.setSubmenu((Menu)w);
			proxy.setTitle(((com.nabla.wapp.client.ui.Menu)w).getTitle());
			proxy.setIcon(((com.nabla.wapp.client.ui.Menu)w).getIcon());
			menu.addItem(proxy);
		} else if (w instanceof MenuItem)
			menu.addItem(((MenuItem) w).getImpl());
		else if (w instanceof MenuItemSeparator)
			menu.addItem(((MenuItemSeparator) w).getImpl());
		else {
			logger.log(Level.SEVERE, "adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
		}
	}

	@Override
	public void clear() {
	}

	@Override
	public Iterator<Widget> iterator() {
        return null;
	}

	@Override
	public boolean remove(@SuppressWarnings("unused") final Widget w) {
		logger.log(Level.SEVERE, "removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public void onCreate() {
	}

}
