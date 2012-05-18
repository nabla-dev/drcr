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
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.smartgwt.client.widgets.Canvas;

/**
 * @author nabla
 *
 */
public class Tab extends Canvas implements IHasWidgets, IPostCreateProcessing {

	private static final Logger							logger = LoggerFactory.getLog(Tab.class);
	private final com.smartgwt.client.widgets.tab.Tab	impl = new com.smartgwt.client.widgets.tab.Tab();
	private String										title = null;
	private String										icon = null;

	@Override
	public Canvas[] getChildren() {
		final Canvas child = impl.getPane();
		if (child == null)
			return new Canvas[0];
		final Canvas[] children = new Canvas[1];
		children[0] = child;
		return children;
	}

	@Override
	public void add(Widget w){
		Helper.onCreate(w);
		if (w instanceof Canvas)
			impl.setPane((Canvas)w);
		else
			logger.warning("adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
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
		logger.warning("removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public <T> T findChild(Class<T> type, boolean recursive) {
		return IHasWidgets.Helper.findChild(getChildren(), type, recursive);
	}

	public void setIcon(String icon) {
		this.icon = icon;
		doSetTitle();
	}

	public String getIcon() {
		return icon;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		doSetTitle();
	}

	public void setCanClose(boolean canClose) {
		impl.setCanClose(canClose);
	}

	public com.smartgwt.client.widgets.tab.Tab getImpl() {
		return impl;
	}

	private void doSetTitle() {
		// NOTE: BUG in SmartClient - cannot set icon when close box is shown!
		// TODO: remove this workaround when bug is fixed
		if (icon == null)
			impl.setTitle(title);
		else if (title != null)
			impl.setTitle("<span>" + Canvas.imgHTML(icon) + " " + title + "</span>");
		else
			impl.setTitle("<span>" + Canvas.imgHTML(icon) + "</span>");
	}

	@Override
	public void onCreate() {
	}

}
