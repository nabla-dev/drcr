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


import com.nabla.dc.shared.command.UserName;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.shared.command.UpdateUserDefinition;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.SelectionDelta;

/**
 * @author nabla
 *
 */
public class UserDefinitionModel extends RoleDefinitionModel {

	static public class Fields {
		public static String roles() { return "roles"; }
	}

	private final Integer	objectId;

	public UserDefinitionModel(final Integer objectId, final Integer roleId) {
		super(roleId);
		this.objectId = objectId;
	}

	public UserDefinitionModel(final Integer roleId) {
		this(null, roleId);
	}

	@Override
	public Model getTreeModel() {
		return new UserDefinitionTreeModel(objectId, roleId);
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final RoleDefinitionRecord record) {
		final SelectionDelta delta = record.getDefinitionDelta();
		if (delta == null || delta.isEmpty())
			return null;	// save a round trip to the server
		return new UpdateUserDefinition(objectId, roleId, delta);
	}

	@Override
	public IAction<StringResult> getRoleName() {
		return new UserName(roleId);
	}
}
