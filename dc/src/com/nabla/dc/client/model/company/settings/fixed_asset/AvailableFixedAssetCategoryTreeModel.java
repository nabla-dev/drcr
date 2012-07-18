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
package com.nabla.dc.client.model.company.settings.fixed_asset;


import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.HomogeneousTreeModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.TreeParentIdField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * @author nabla
 *
 */
public class AvailableFixedAssetCategoryTreeModel extends HomogeneousTreeModel<TreeNode> {

	static public class Fields {
		public String name() { return IFixedAssetCategory.NAME; }
	}

	private static final Fields	fields = new Fields();
	protected final Integer		companyId;

	public AvailableFixedAssetCategoryTreeModel(final Integer companyId) {
		this.companyId = companyId;

		setFields(
			new IdField(),
			new TreeParentIdField(),
			new TextField(fields.name(), FieldAttributes.READ_ONLY)
				);
	}

	public Fields fields() {
		return fields;
	}
/*
	@Override
	public AbstractFetch getFetchCommand(final DSRequest request) {
		return new FetchRoleDefinition(companyId);
	}
*/
}
