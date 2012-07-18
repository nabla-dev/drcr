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
package com.nabla.dc.client.model.options;


import com.nabla.wapp.client.model.UserRecord;
import com.nabla.wapp.shared.command.CloneUser;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class CloneUserModel extends AddUserModel {

	private final Integer	fromUserId;

	public CloneUserModel(final Integer fromUserId) {
		super();
		this.fromUserId = fromUserId;
	}

	@Override
	public IAction<StringResult> getAddCommand(final UserRecord user) {
		return new CloneUser(fromUserId, user.getName(), user.getPassword());
	}

}
