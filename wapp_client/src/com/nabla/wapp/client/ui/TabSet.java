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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.ISlotManager1;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;

/**
 * @author nabla
 *
 */
public class TabSet extends com.smartgwt.client.widgets.tab.TabSet implements IHasWidgets, IPostCreateProcessing {

	private static final Logger								logger = LoggerFactory.getLog(TabSet.class);
	final List<Object>										tabBarControls = new LinkedList<Object>();
	final Map<com.smartgwt.client.widgets.tab.Tab, Tab>		tabs = new HashMap<com.smartgwt.client.widgets.tab.Tab, Tab>();
	final Signal1<Tab>										sigTabClosed = new Signal1<Tab>();

	public TabSet() {
		tabBarControls.add(TabBarControls.TAB_SCROLLER);
		tabBarControls.add(TabBarControls.TAB_PICKER);
	//	this.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER);
		this.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				final com.smartgwt.client.widgets.tab.Tab tab = event.getTab();
				Assert.notNull(tab != null);
				sigTabClosed.fire(tabs.get(tab));
				tabs.remove(tab);
			}
		});
	}

	@Override
	public Canvas[] getChildren() {
		final com.smartgwt.client.widgets.tab.Tab[] children = this.getTabs();
		final Canvas[] rslt = new Canvas[children.length];
		for (int i = 0; i < children.length; ++i)
			rslt[i] = tabs.get(children[i]);
		return rslt;
	}

	@Override
	public void add(Widget w){
		Assert.argumentNotNull(w);

		if (w instanceof Tab)
			this.addTab((Tab)w);
		else {
			tabBarControls.add(w);
//			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
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
	public boolean remove(@SuppressWarnings("unused") Widget w) {
		logger.log(Level.SEVERE,"removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public <T> T findChild(Class<T> type, boolean recursive) {
		return IHasWidgets.Helper.findChild(this.getChildren(), type, recursive);
	}

	public void addTab(final Tab tab) {
		com.smartgwt.client.widgets.tab.Tab impl = tab.getImpl();
		super.addTab(impl);
		tabs.put(impl, tab);
	}

	public void addTab(final Tab tab, int position) {
		com.smartgwt.client.widgets.tab.Tab impl = tab.getImpl();
		super.addTab(impl, position);
		tabs.put(impl, tab);
	}

	public void selectTab(final Tab tab) {
		super.selectTab(tab.getImpl());
	}

	public ISlotManager1<Tab> getTabClosedsSlots() {
		return sigTabClosed;
	}

	@Override
	public void onCreate() {
		this.setTabBarControls(tabBarControls.toArray());
	}

}
