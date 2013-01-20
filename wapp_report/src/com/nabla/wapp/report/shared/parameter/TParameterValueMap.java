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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TParameterValueMap<T> implements IParameterValueMap {

	// TODO LinkedHashMap order not preserved by GWT RPC!!!
	private Map<T, String>		impl = new HashMap<T, String>();
	private List<T>				order = new LinkedList<T>();

	public void put(final T key, final String value) {
		impl.put(key, value);
		order.add(key);
	}

	@Override
	public Map<String, String> get() {
		final Map<String, String> m = new LinkedHashMap<String, String>();
		for (T k : order)
			m.put(k.toString(), impl.get(k));
		return m;
	}

	@Override
	public boolean containsKey(Object key) {
		return impl.containsKey(key);
	}

	@Override
	public String get(Object key) {
		return impl.get(key);
	}

}
