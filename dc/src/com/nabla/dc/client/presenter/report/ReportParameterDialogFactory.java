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
package com.nabla.dc.client.presenter.report;

import java.util.List;

import com.nabla.dc.client.ui.report.ReportParameterDialogUi;
import com.nabla.wapp.report.client.IReportParameterBinder;
import com.nabla.wapp.report.client.presenter.ReportParameterDialog;

public class ReportParameterDialogFactory implements ReportParameterDialog.IFactory {

	@Override
	public ReportParameterDialog get(final List<IReportParameterBinder> parameterBinders) {
		return new ReportParameterDialog(new ReportParameterDialogUi(parameterBinders));
	}

}
