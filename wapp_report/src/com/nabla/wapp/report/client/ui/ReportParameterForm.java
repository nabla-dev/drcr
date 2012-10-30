/**
* Copyright 2011 nabla
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
package com.nabla.wapp.report.client.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.client.IReportParameterBinder;
import com.nabla.wapp.report.shared.ReportParameterValueList;
import com.nabla.wapp.report.shared.command.GetReportParameterDefaultValues;
import com.nabla.wapp.shared.signal.Signal1;
import com.nabla.wapp.shared.slot.ISlot;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * The <code></code> object is used to
 *
 */
public class ReportParameterForm extends Form {

	private final Signal1<ReportParameterValueList>		sigSuccess = new Signal1<ReportParameterValueList>();
	private final List<IReportParameterBinder>			parameterBinders;

	public ReportParameterForm(final List<IReportParameterBinder> parameterBinders) {
		this.parameterBinders = parameterBinders;
		for (IReportParameterBinder e : parameterBinders)
			e.createFormItem(this);
		this.getSubmitSlots().connect(onSubmit);
	}

	public ISlotManager1<ReportParameterValueList> getSubmitSlotManager() {
		return sigSuccess;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		final Set<String> parameterNames = new HashSet<String>();
		for (IReportParameterBinder e : parameterBinders)
			e.getNeedDefaultValue(parameterNames);
		if (parameterNames.isEmpty())
			editNewRecord();
		else
			editNewRecordWithDefault(new GetReportParameterDefaultValues(parameterNames));
	}

	private final ISlot onSubmit = new ISlot() {
		@Override
		public void invoke() {
			final ReportParameterValueList parameters = new ReportParameterValueList();
			for (IReportParameterBinder e : parameterBinders) {
				final Map<String, String> errors = e.getValue(ReportParameterForm.this, parameters);
				if (errors != null) {
					setErrors(errors, true);
					return;
				}
			}
			sigSuccess.fire(parameters);
		}
	};

}
