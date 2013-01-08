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
package com.nabla.wapp.report.client.presenter;

import java.util.List;

import com.nabla.wapp.client.mvp.AbstractTopPresenter;
import com.nabla.wapp.client.mvp.ITopDisplay;
import com.nabla.wapp.report.client.IReportParameterBinder;
import com.nabla.wapp.report.shared.ReportParameterValueList;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager1;


public class ReportParameterDialog extends AbstractTopPresenter<ReportParameterDialog.IDisplay> {

	public interface IFactory {
		ReportParameterDialog get(final List<IReportParameterBinder> parameterBinders);
	}

	public interface IDisplay extends ITopDisplay {
		ISlotManager1<ReportParameterValueList> getSubmitSlots();
	}

	public ReportParameterDialog(final IDisplay ui) {
		super(ui);
	}

	public ISlotManager1<ReportParameterValueList> getSubmitSlots() {
		return getDisplay().getSubmitSlots();
	}

	@Override
	public void bind() {
		super.bind();
		getDisplay().getSubmitSlots().connect(onSubmit);
	}

	private final ISlot onSubmit = new ISlot() {
		@Override
		public void invoke() {
			getDisplay().hide();
		}
	};

}
