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

import com.google.gwt.event.shared.HandlerRegistration;
import com.nabla.wapp.shared.slot.IBasicSlot;
import com.nabla.wapp.shared.slot.IBasicSlotManager;

/**
 * @author nabla64
 *
 */
public interface IPresenter {

	/**
	 * Called then the presenter is initialised. This is called before any other
	 * methods. Any event handlers and other setup should be done here rather
	 * than in the constructor.
	 */
	void bind();

	/**
	 * Called after the presenter and display have been finished with for the
	 * moment.
	 */
	void unbind();

	/**
	 * Returns the {@link IDisplay} for the current presenter.
	 *
	 * @return The display.
	 */
	IDisplay getDisplay();

	/**
	 * Register handler so that its life is contained between bind() and unbind()
	 * @param handler	- handler to register
	 */
	void registerHandler(HandlerRegistration handler);

	/**
	 * Register signal/slot connection handler so that its life is contained between bind() and unbind()
	 * @param signal	- signal
	 * @param slot		- slot
	 */
	<S extends IBasicSlot> void registerSlot(IBasicSlotManager<S> signal, S slot);

}
