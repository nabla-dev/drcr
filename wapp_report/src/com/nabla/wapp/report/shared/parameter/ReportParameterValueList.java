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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The <code></code> object is used to
 *
 */
public class ReportParameterValueList extends LinkedList<IParameterValue> implements IsSerializable, IParameterValue {

	private static final long serialVersionUID = 1L;

	public ReportParameterValueList() {}

	public ReportParameterValueList(final List<IParameterValue> values) {
		super(values);
	}

	public ReportParameterValueList(final IParameterValue value) {
		add(value);
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
		for (IParameterValue e : this)
			e.addToMap(parameters);
	}

	@Override
	public void addToStringMap(Map<String, String> parameters) {
		for (IParameterValue e : this)
			e.addToStringMap(parameters);
	}

}
