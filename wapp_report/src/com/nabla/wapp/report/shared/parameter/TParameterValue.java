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

import java.util.Map;



/**
 * The <code></code> object is used to
 *
 */
public class TParameterValue<T> implements IParameterValue {

	protected String	name;
	protected T		value;

	TParameterValue() {}	// for serialization only

	public TParameterValue(final String name, final T value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void addToMap(Map<String, Object> parameters) {
		parameters.put(name, value);
	}

	@Override
	public void addToStringMap(Map<String, String> parameters) {
		parameters.put(name, getValueAsString());
	}

	protected String getValueAsString() {
		return (value == null) ? "" : value.toString();
	}

}
