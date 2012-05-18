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
package com.nabla.wapp.shared.auth;

import com.nabla.wapp.shared.dispatch.IResult;
import com.nabla.wapp.shared.general.StringSet;

/**
 * The <code>RoleSetResult</code> object is used to return
 * the list of roles supported by a user
 * @author nabla
 *
 */
public class RoleSetResult implements IResult {

	private static final long serialVersionUID = 1L;

	private StringSet	value;

	public RoleSetResult() {}	// for serialization only

	/**
	 * Constructor for the <code>RoleSetResult</code> object.
	 * @param value	- list of supported roles
	 */
	public RoleSetResult(final StringSet value) {
		this.value = value;
	}

	/**
	 * To get list of supported roles
	 * @return this returns the list of supported roles
	 */
	public StringSet get() {
		return value;
	}

}
