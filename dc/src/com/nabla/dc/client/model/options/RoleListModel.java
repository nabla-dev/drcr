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
import com.nabla.wapp.client.model.data.RoleRecord;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.command.AddRole;
import com.nabla.wapp.shared.command.FetchRoleList;
import com.nabla.wapp.shared.command.RemoveRole;
import com.nabla.wapp.shared.command.UpdateRole;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IRole;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class RoleListModel extends CModel<RoleRecord> {

	static public class Fields {
		public String name() { return IRole.NAME; }
		public String builtIn() { return IRole.INTERNAL; }
	}

	private static final Fields	fields = new Fields();

	public RoleListModel() {
		super(RoleRecord.factory);

		setFields(
			new IdField(),
			new TextField(IRole.NAME, IRole.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new BooleanField(IRole.INTERNAL, FieldAttributes.READ_ONLY)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveRole();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchRoleList();
	}

	@Override
	public IAction<StringResult> getAddCommand(final RoleRecord record) {
		return new AddRole(record.getName());
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final RoleRecord record) {
		return new UpdateRole(record.getId(), record.getName());
	}

}
