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

/**
 * @author FNorais
 *
 */
public class RowMap implements IRowMap {

	private final Map<String, Integer>	fieldRows = new HashMap<String, Integer>();

	@Override
	public Integer get(String fieldName) {
		return fieldRows.get(fieldName);
	}

	@Override
	public void put(String fieldName, Integer value) {
		fieldRows.put(fieldName, value);
	}

}
