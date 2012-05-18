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


/**
 * @author nabla
 *
 */
public interface IRolePrivileges {

	static final String	USER_VIEW = "user_view";
	static final String	USER_ADD = "user_add";
	static final String	USER_REMOVE = "user_remove";
	static final String	USER_EDIT = "user_edit";

	static final String	ROLE_VIEW = "role_view";
	static final String	ROLE_ADD = "role_add";
	static final String	ROLE_REMOVE = "role_remove";
	static final String	ROLE_EDIT = "role_edit";

}
