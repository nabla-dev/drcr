/**
* Copyright 2013 nabla
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
package com.nabla.dc.client.presenter.report;

import com.nabla.dc.client.ui.report.AddReportDialogUi;
import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.shared.slot.ISlot1;

public class AddReportDialog extends AbstractTopPresenter<AddReportDialog.IDisplay> {

	public interface IDisplay extends ITopDisplay {
		void cleanup();
	}

	public AddReportDialog(final ISlot1<Integer> onSuccess) {
		super(new AddReportDialogUi(onSuccess));
	}

	@Override
	public void unbind() {
		getDisplay().cleanup();
		super.unbind();
	}

}
