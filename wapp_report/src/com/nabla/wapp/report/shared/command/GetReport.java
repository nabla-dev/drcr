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
package com.nabla.wapp.report.shared.command;

import java.util.Set;

import com.nabla.wapp.report.shared.ReportOptions;
import com.nabla.wapp.report.shared.ReportResult;
import com.nabla.wapp.report.shared.parameter.ParameterValueList;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.print.ReportFormats;

public class GetReport extends ReportOptions implements IAction<ReportResult> {

	private IntegerSet						reportIds;
	private ParameterValueList		parameters;

	GetReport() {}	// for serialization only

	public GetReport(final Set<Integer> reportIds, final ReportFormats format, final Boolean outputAsFile, final ParameterValueList parameters) {
		super(format, outputAsFile);
		this.reportIds = new IntegerSet(reportIds);
		this.parameters = parameters;
	}

	public IntegerSet getReportIds() {
		return reportIds;
	}

	public ParameterValueList getParameters() {
		return parameters;
	}

}
