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
package com.nabla.dc.client.presenter.company;

import com.nabla.dc.client.model.company.AddCompanyRecord;
import com.nabla.dc.client.ui.company.AddCompanyDialogUi;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.shared.slot.ISlot1;
import com.nabla.wapp.shared.slot.ISlotManager1;


public class AddCompanyDialog extends AbstractTopPresenter<AddCompanyDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		ISlotManager1<AddCompanyRecord> getSuccessSlots();
	}

	public AddCompanyDialog(final IDisplay ui) {
		super(ui);
	}

	public AddCompanyDialog(final IDisplay ui, final ISlot1<AddCompanyRecord> successSlot) {
		this(ui);
		getSuccessSlots().connect(successSlot);
	}

	public AddCompanyDialog(final ISlot1<AddCompanyRecord> successSlot) {
		this(new AddCompanyDialogUi(), successSlot);
	}

	public ISlotManager1<AddCompanyRecord> getSuccessSlots() {
		return getDisplay().getSuccessSlots();
	}

}
