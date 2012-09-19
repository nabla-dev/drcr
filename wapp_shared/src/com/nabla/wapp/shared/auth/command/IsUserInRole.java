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
package com.nabla.wapp.shared.auth.command;

import com.nabla.wapp.shared.auth.RoleSetResult;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.general.StringSet;


/**
 * The <code>IsUserInRole</code> object is used to query
 * if a user supports a list of roles
 * @author nabla64
 *
 */
public class IsUserInRole implements IAction<RoleSetResult> {

	@Nullable Integer	objectId;
	StringSet			roles;

	public IsUserInRole() {}	// for serialization only

	/**
	 * Constructor for <code>IsUserInRole</code> object.
	 * @param objectId	- object ID to which roles need to be applied to
	 * @param roles	    - list of roles to query
	 */
	public IsUserInRole(@Nullable final Integer objectId, final StringSet roles) {
		this.objectId = objectId;
		this.roles = roles;
	}

	/**
	 * To get the object ID to which roles need to be applied to
	 * @return this returns the object ID
	 */
	public @Nullable Integer getObjectId() {
		return objectId;
	}

	/**
	 * To get list of roles to test
	 * @return this returns the list of roles to test
	 */
	public StringSet getRoles() {
		return roles;
	}

}
