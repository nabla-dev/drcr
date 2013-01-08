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

public interface ISlotManager3<T0, T1, T2> extends IBasicSlotManager<ISlot3<T0, T1, T2>> {

	@Override
	Connection<ISlot3<T0, T1, T2>> connect(final ISlot3<T0, T1, T2> slot);
	@Override
	Connection<ISlot3<T0, T1, T2>> connect(final ISlot3<T0, T1, T2> slot, int at);
	Connection<ISlot3<T0, T1, T2>> connect(final ISlot slot);
	Connection<ISlot3<T0, T1, T2>> connect(final ISlot slot, int at);

}
