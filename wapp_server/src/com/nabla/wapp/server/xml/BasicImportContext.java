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
package com.nabla.wapp.server.xml;

import java.util.HashMap;
import java.util.Map;

import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author FNorais
 *
 */
public class BasicImportContext implements IImportContext {

	protected final IErrorList<Integer>		errors;
	private final Map<Integer, IRowMap>		rowMap = new HashMap<Integer, IRowMap>();

	public BasicImportContext(final IErrorList<Integer> errors) {
		this.errors = errors;
	}

	@Override
	public IErrorList<Integer> getErrorList() {
		return errors;
	}

	@Override
	public IRowMap getRowMap(Integer id) {
		IRowMap m = rowMap.get(id);
		if (m == null) {
			m = new RowMap();
			rowMap.put(id, m);
		}
		return m;
	}

}
