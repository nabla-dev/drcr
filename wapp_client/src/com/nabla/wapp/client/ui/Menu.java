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


public class Menu extends com.smartgwt.client.widgets.menu.Menu implements HasWidgets, IPostCreateProcessing {

	private static final Logger	logger = LoggerFactory.getLog(Menu.class);
	private String					title;
	private String					icon;

	public Menu() {}

	@Override
	public void add(Widget w){
		Assert.argumentNotNull(w);

		if (w instanceof MenuItem)
			this.addItem(((MenuItem) w).getImpl());
		else if (w instanceof MenuItemSeparator)
			this.addItem(((MenuItemSeparator) w).getImpl());
		else {
			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
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
	public boolean remove(@SuppressWarnings("unused") Widget w) {
		logger.log(Level.SEVERE,"removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}
}
