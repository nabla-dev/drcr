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
package com.nabla.wapp.report.shared;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * The <code></code> object is used to
 *
 */
public class IntegerSetReportParameterValue implements IReportParameterValue {

	private String				name;
	private HashSet<Integer>	values;

	public IntegerSetReportParameterValue() {}

	public IntegerSetReportParameterValue(String name, Integer... values) {
		this.name = name;
		if (values != null)
			this.values = new HashSet<Integer>(Arrays.asList(values));
	}

	public IntegerSetReportParameterValue(String name, Collection<Integer> values) {
		this.name = name;
		if (values != null)
			this.values = new HashSet<Integer>(values);
	}

	@Override
	public void addToMap(Map<String, Object> parameters) {
		parameters.put(name, values);
	}

	@Override
	public void addToStringMap(Map<String, String> parameters) {
		parameters.put(name, getValueAsString());
	}

	private String getValueAsString() {
		return (values == null) ? "" : values.toString();
	}
}
