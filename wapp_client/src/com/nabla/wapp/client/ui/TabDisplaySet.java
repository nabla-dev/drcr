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
import java.util.Map;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager1;

public class TabDisplaySet extends TabSet {

	private final Map<Tab, ITabDisplay>	displays = new HashMap<Tab, ITabDisplay>();
	private final Signal1<ITabDisplay>		sigTabClosed = new Signal1<ITabDisplay>();

	public TabDisplaySet() {
		super.getTabClosedsSlots().connect(new ISlot1<Tab>() {
			@Override
			public void invoke(final Tab tab) {
				sigTabClosed.fire(displays.get(tab));
				displays.remove(tab);
			}
		});
	}
	
	public void addTab(final ITabDisplay tab) {
		Assert.argumentNotNull(tab);

		final Tab tabImpl = tab.getImpl();
		super.addTab(tabImpl);
		super.selectTab(tabImpl);
		displays.put(tabImpl, tab);
	}
	
	public ISlotManager1<ITabDisplay> getTabClosedSlots() {
		return sigTabClosed;
	}
	
}
