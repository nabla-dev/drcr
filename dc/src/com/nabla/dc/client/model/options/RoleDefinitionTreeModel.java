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
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.TreeParentIdField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.FetchRoleDefinition;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.JSOHelper;

/**
 * @author nabla
 *
 */
public class RoleDefinitionTreeModel extends CModel<Record> {

	static public class Fields {
		public String name() { return "name"; }
		public static String include() { return "isIncluded"; }
	}

	protected final Integer	roleId;

	public RoleDefinitionTreeModel(final Integer roleId) {
		this.roleId = roleId;

		setFields(
			new IdField(),
			new TreeParentIdField(),
			new TextField("name", FieldAttributes.READ_ONLY),
			new BooleanField(Fields.include())
				);
	}

	@Override
	public AbstractFetch getFetchCommand(final DSRequest request) {
		return new FetchRoleDefinition(roleId, getParentId(request));
	}

	protected Integer getParentId(final DSRequest request) {
		return JSOHelper.getAttributeAsInt(request.getData(), IFieldReservedNames.TREEGRID_PARENT_ID);
	}

}
