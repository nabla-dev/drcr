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

import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.StringSet;

/**
 * The <code></code> object is used to
 *
 */
public class GetReportParameterDefaultValues implements IAction<StringResult> {

	private StringSet	parameterNames;

	GetReportParameterDefaultValues() {}	// for serialization only

	public GetReportParameterDefaultValues(final Set<String> parameterNames) {
		this.parameterNames = new StringSet(parameterNames);
	}

	public StringSet getParameterNames() {
		return parameterNames;
	}

}
