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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerRegistration;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.shared.slot.IBasicSlot;
import com.nabla.wapp.shared.slot.IBasicSlotManager;

/**
 * @author nabla64
 *
 */
public abstract class AbstractPresenter<D extends IDisplay> implements IPresenter {

	private static final Logger				logger = LoggerFactory.getLog(AbstractPresenter.class);
	private final D							display;
	private final List<HandlerRegistration>	handlers = new ArrayList<HandlerRegistration>();

	protected AbstractPresenter(D display) {
		this.display = display;
	}

	@Override
	public void registerHandler(HandlerRegistration handler) {
		handlers.add(handler);
	}

	@Override
	public <S extends IBasicSlot> void registerSlot(IBasicSlotManager<S> signal, S slot) {
		Assert.argumentNotNull(signal);

		registerHandler(signal.connect(slot));
	}

	@Override
	public void bind() {
		logger.info("binding " + Util.getClassSimpleName(this.getClass()));
	}

	@Override
	public void unbind() {
		logger.info("un-binding " + Util.getClassSimpleName(this.getClass()));
		for (HandlerRegistration handler : handlers)
			handler.removeHandler();
		handlers.clear();
	}

	@Override
	public D getDisplay() {
		return display;
	}

}
