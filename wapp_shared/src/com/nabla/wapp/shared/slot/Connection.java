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

package com.nabla.wapp.shared.slot;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author nabla
 *
 */
public class Connection<Slot extends IBasicSlot> implements HandlerRegistration {

	private Slot		slot;
	private boolean		isBlocked = false;

	Connection(final Slot __slot) {
		slot = __slot;
	}

	public boolean isConnected() {
		return slot != null;
	}

	public void disconnect() {
		slot = null;
	}

	public void block(){
		isBlocked = true;
	}
	public void block(boolean b) {
		isBlocked = b;
	}
	public void unblock() {
		isBlocked = false;
	}
	public boolean isBlocked() {
		return isBlocked;
	}

	Slot slot() {
		return slot;
	}

	@Override
	public void removeHandler() {
		disconnect();
	}
}
