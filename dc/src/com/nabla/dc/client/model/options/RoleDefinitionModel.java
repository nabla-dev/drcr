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


import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.FetchRoleName;
import com.nabla.wapp.shared.command.UpdateRoleDefinition;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.SelectionDelta;
import com.nabla.wapp.shared.model.IRole;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class RoleDefinitionModel extends CModel<RoleDefinitionRecord> {

	static public class Fields {
		public String name() { return IRole.NAME; }
		public static String roles() { return "roles"; }
	}

	protected final Integer	roleId;

	public RoleDefinitionModel(final Integer roleId) {
		super(RoleDefinitionRecord.factory);
		this.roleId = roleId;
	}

	public Model getTreeModel() {
		return new RoleDefinitionTreeModel(roleId);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchRoleName(roleId);
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final RoleDefinitionRecord record) {
		final SelectionDelta delta = record.getDefinitionDelta();
		if (delta == null || delta.isEmpty())
			return null;	// save a round trip to the server
		return new UpdateRoleDefinition(roleId, delta);
	}

}
