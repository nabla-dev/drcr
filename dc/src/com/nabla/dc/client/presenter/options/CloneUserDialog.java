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

import com.nabla.dc.client.ui.options.CloneUserDialogUi;
import com.nabla.wapp.client.model.data.UserRecord;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;


public class CloneUserDialog extends AbstractTopPresenter<CloneUserDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		void clearConfirmPassword();
		ISlotManager getFailureSlots();
		ISlotManager1<UserRecord> getSuccessSlots();
	}

	public CloneUserDialog(final IDisplay ui) {
		super(ui);
	}

	public CloneUserDialog(final Integer fromUserId) {
		super(new CloneUserDialogUi(fromUserId));
	}

	@Override
	public void bind() {
		super.bind();
		registerSlot(getDisplay().getFailureSlots(), onFailure);
	}

	public ISlotManager1<UserRecord> getSuccessSlots() {
		return getDisplay().getSuccessSlots();
	}

	private final ISlot onFailure = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().clearConfirmPassword();
		}
	};

}
