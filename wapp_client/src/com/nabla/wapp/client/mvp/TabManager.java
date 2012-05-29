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
package com.nabla.wapp.client.mvp;

import java.util.HashMap;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.shared.slot.ISlot1;

/**
 * @author nabla64
 *
 */
public class TabManager extends HashMap<ITabDisplay, IPresenter> {

	private static final long serialVersionUID = 1L;

	@Override
	public void clear() {
		for (final IPresenter tab : this.values())
			tab.unbind();
		super.clear();
	}
	
	public void remove(final ITabDisplay tab) {
		if (tab != null && this.containsKey(tab)) {
			get(tab).unbind();
			super.remove(tab);
		}
	}
	
	public <D extends ITabDisplay> ITabDisplay add(final AbstractTabPresenter<D> tab) {
		Assert.argumentNotNull(tab);

		tab.bind();
		final ITabDisplay display = tab.getDisplay();
		put(display, tab);
		return display;
	}
	
	public ISlot1<ITabDisplay> getTabClosedSlot() {
		return onTabClosed;
	}
	
	private final ISlot1<ITabDisplay> onTabClosed = new ISlot1<ITabDisplay>() {
		@Override
		public void invoke(final ITabDisplay tab) {
			remove(tab);
		}
	};
}
