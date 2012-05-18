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
package com.nabla.wapp.shared.command;

import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IntegerResult;


/**
 * @author nabla64
 *
 */
public class AddListGridFilter implements IAction<IntegerResult> {

	private static final long serialVersionUID = 1L;

	private String	filter;
	private String	name;
	private String	value;

	public AddListGridFilter() {}

	public AddListGridFilter(final String filter, final String name, final String value) {
		this.filter = filter;
		this.name = name;
		this.value = value;
	}

	public String getFilter() {
		return filter;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
