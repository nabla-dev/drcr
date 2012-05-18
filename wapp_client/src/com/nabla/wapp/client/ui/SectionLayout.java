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

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.smartgwt.client.widgets.layout.SectionStack;

/**
 * The <code></code> object is used to
 *
 */
public class SectionLayout extends SectionStack implements IHasWidgets, IPostCreateProcessing {

	private static final Logger	logger = LoggerFactory.getLog(SectionLayout.class);

	@Override
	public void add(Widget w){
		Helper.onCreate(w);
		if (w instanceof SectionPanel)
			this.addSection(((SectionPanel)w).getImpl());
		else
			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
	}

	@Override
	public void clear() {
		IHasWidgets.Helper.clear(this);
	}

	@Override
	public Iterator<Widget> iterator() {
        return IHasWidgets.Helper.iterator(this.getMembers());
	}

	@Override
	public boolean remove(Widget w) {
        return IHasWidgets.Helper.remove(this, w);
	}

	@Override
	public <T> T findChild(Class<T> type, boolean recursive) {
		return IHasWidgets.Helper.findChild(this.getMembers(), type, recursive);
	}

	@Override
	public void onCreate() {

	}

}
