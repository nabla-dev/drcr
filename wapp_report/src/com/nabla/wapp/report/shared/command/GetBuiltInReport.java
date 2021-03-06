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

import com.nabla.wapp.report.shared.ReportOptions;
import com.nabla.wapp.report.shared.parameter.IParameterValue;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IntegerResult;
import com.nabla.wapp.shared.print.ReportFormats;


public class GetBuiltInReport extends ReportOptions implements IAction<IntegerResult> {

	private String				internalName;
	private IParameterValue		parameter;

	GetBuiltInReport() {}	// for serialization only

	public GetBuiltInReport(final String internalName, final ReportFormats format, final Boolean outputAsFile, final IParameterValue parameter) {
		super(format, outputAsFile);
		this.internalName = internalName;
		this.parameter = parameter;
	}

	public String getName() {
		return internalName;
	}

	public Map<String, Object> getParameters() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		if (parameter != null)
			parameter.addToMap(ret);
		return ret;
	}

}
