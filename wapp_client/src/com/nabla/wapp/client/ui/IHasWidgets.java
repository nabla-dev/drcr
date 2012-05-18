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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.widgets.Canvas;

/**
 * @author nabla
 *
 */
public interface IHasWidgets extends HasWidgets {

	class Helper {
		private static final Logger	logger = LoggerFactory.getLog(Helper.class);

		static public void clear(final Canvas parent) {
			Assert.argumentNotNull(parent);

			for(Canvas w: parent.getChildren())
				parent.removeChild(w);
		}

		static public Iterator<Widget> iterator(final Canvas parent){
			Assert.argumentNotNull(parent);

			return iterator(parent.getChildren());
		}

		static public Iterator<Widget> iterator(final Canvas[] children){
			final List<Widget> tmp = new ArrayList<Widget>();
			if (children != null) {
				for(Canvas w: children)
					tmp.add(w);
			}
			return tmp.iterator();
		}

		static public boolean remove(final Widget parent, final Widget w){
			Assert.argumentNotNull(w);

			if (w.getParent() != parent)
				return false;
			w.removeFromParent();
			return true;
		}

		@SuppressWarnings("unchecked")
		static public <T, E extends Object> T findChild(final E[] children, Class<T> type, boolean recursive) {
			for (E c : children) {
				if (c.getClass().getName().equals(type.getName()))
					return (T)c;
			}
			if (recursive) {
				for (E c : children) {
					if (c instanceof IHasWidgets) {
						final T rslt = ((IHasWidgets)c).findChild(type, recursive);
						if (rslt != null)
							return rslt;
					} else if (c instanceof Canvas) {
						final T rslt = findChild(((Canvas)c).getChildren(), type, recursive);
						if (rslt != null)
							return rslt;
					}
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		static public <T> T findChild(final Collection<? extends Object> children, Class<T> type, boolean recursive) {
			Iterator<?> i = children.iterator();
			while (i.hasNext()) {
				Object c = i.next();
				if (c.getClass().getName().equals(type.getName()))
					return (T)c;
			}
			if (recursive) {
				i = children.iterator();
				while (i.hasNext()) {
					Object c = i.next();
					if (c instanceof IHasWidgets) {
						final T rslt = ((IHasWidgets)c).findChild(type, recursive);
						if (rslt != null)
							return rslt;
					} else if (c instanceof Canvas) {
						final T rslt = findChild(((Canvas)c).getChildren(), type, recursive);
						if (rslt != null)
							return rslt;
					}
				}
			}
			return null;
		}

		static public void onCreate(final Widget w) {
			Assert.argumentNotNull(w);

			if (w instanceof IPostCreateProcessing) {
				logger.finer("onCreate: " + w.getClass().getName());
				((IPostCreateProcessing)w).onCreate();
			}
		}
	}

	<T> T findChild(Class<T> type, boolean recursive);

}
