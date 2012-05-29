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
package com.nabla.dc.client.presenter.options;

import com.nabla.dc.client.ui.options.ChangeUserPasswordDialogUi;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;

/**
 * @author nabla
 *
 */
public class ChangeUserPasswordDialog extends AbstractTopPresenter<ChangeUserPasswordDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		void clearConfirmPassword();
		ISlotManager getFailureSlots();
	}

	public ChangeUserPasswordDialog(final IDisplay ui) {
		super(ui);
	}

	public ChangeUserPasswordDialog(final String userName) {
		super(new ChangeUserPasswordDialogUi(userName));
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerSlot(getDisplay().getFailureSlots(), onFailure);
	}

	private final ISlot onFailure = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().clearConfirmPassword();
		}
	};

}
