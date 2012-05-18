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



/**
 * @author nabla
 *
 */
public class SlotManager1<T0> extends ConnectionList<ISlot1<T0>> implements ISlotManager1<T0> {

	@Override
	public Connection<ISlot1<T0>> connect(final ISlot1<T0> slot) {
		return super.connect(slot);
	}

	@Override
	public Connection<ISlot1<T0>> connect(final ISlot1<T0> slot, int at) {
		return super.connect(slot, at);
	}

	@Override
	public Connection<ISlot1<T0>> connect(final ISlot slot) {
		return connect(new ISlot1<T0>() {
			@Override
			public void invoke(@SuppressWarnings("unused") T0 a0) {
				slot.invoke();
			}
		});
	}

	@Override
	public Connection<ISlot1<T0>> connect(final ISlot slot, int at) {
		return connect(new ISlot1<T0>() {
			@Override
			public void invoke(@SuppressWarnings("unused") T0 a0) {
				slot.invoke();
			}
		}, at);
	}

}
