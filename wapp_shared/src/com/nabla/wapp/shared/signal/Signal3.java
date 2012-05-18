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

package com.nabla.wapp.shared.signal;

import com.nabla.wapp.shared.slot.ISlot3;
import com.nabla.wapp.shared.slot.ISlotIterator;
import com.nabla.wapp.shared.slot.SlotManager3;

/**
 * @author nabla
 *
 */
public class Signal3<T0, T1, T2> extends SlotManager3<T0, T1, T2> implements ISignal3<T0, T1, T2> {

	@Override
	public void fire(T0 a0, T1 a1, T2 a2) {
		final ISlotIterator<ISlot3<T0, T1, T2>> iter = iterator();
		while (iter.hasNext())
			iter.next().invoke(a0, a1, a2);
	}

}
