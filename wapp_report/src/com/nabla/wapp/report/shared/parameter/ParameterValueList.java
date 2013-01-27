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
package com.nabla.wapp.report.shared.parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code></code> object is used to
 *
 */
public class ParameterValueList extends HashMap<String, IParameterValue> implements IParameterValue {

	private static final long serialVersionUID = 1L;

	public ParameterValueList() {}

	public ParameterValueList(final IParameterValue value) {
		add(value);
	}

	public void add(final IParameterValue value) {
		put(value.getParameterName(), value);
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> ret = new HashMap<String, Object>();
		addToMap(ret);
		return ret;
	}

	public Map<String, String> toStringMap() {
		final Map<String, String> ret = new HashMap<String, String>();
		addToStringMap(ret);
		return ret;
	}

	@Override
	public void addToMap(Map<String, Object> parameters) {
		for (IParameterValue e : this.values())
			e.addToMap(parameters);
	}

	@Override
	public void addToStringMap(Map<String, String> parameters) {
		for (IParameterValue e : this.values())
			e.addToStringMap(parameters);
	}

	@Override
	public String getParameterName() {
		return "";
	}

}
