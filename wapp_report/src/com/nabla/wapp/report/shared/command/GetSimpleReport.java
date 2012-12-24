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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nabla.wapp.report.shared.IReportParameterValue;
import com.nabla.wapp.report.shared.ReportOptions;
import com.nabla.wapp.report.shared.SimpleReportResult;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.print.ReportFormats;

/**
 * The <code></code> object is used to
 *
 */
public class GetSimpleReport extends ReportOptions implements IAction<SimpleReportResult> {

	private IntegerSet				reportIds;
	private IReportParameterValue	parameter;

	GetSimpleReport() {}	// for serialization only

	public GetSimpleReport(final Set<Integer> reportIds, final IReportParameterValue parameter, final ReportFormats format, final Boolean outputAsFile, final String locale) {
		super(format, outputAsFile, locale);
		this.reportIds = new IntegerSet(reportIds);
		this.parameter = parameter;
	}

	public IntegerSet getReportIds() {
		return reportIds;
	}

	public Map<String, Object> getParameters() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		if (parameter != null)
			parameter.addToMap(ret);
		return ret;
	}

}
