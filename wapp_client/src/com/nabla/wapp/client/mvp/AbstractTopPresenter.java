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

import com.nabla.wapp.shared.slot.ISlot;


/**
 * @author nabla64
 *
 */
public abstract class AbstractTopPresenter<D extends ITopDisplay> extends AbstractPresenter<D> implements ITopPresenter {

	protected AbstractTopPresenter(D display) {
		super(display);
	}

	@Override
	public void revealDisplay() {
		bind();
		getDisplay().show();
	}

	@Override
	protected void onBind() {
		registerSlot(getDisplay().getHideSlots(), new ISlot() {
			@Override
			public void invoke() {
				unbind();
			}
		});
	}

}
