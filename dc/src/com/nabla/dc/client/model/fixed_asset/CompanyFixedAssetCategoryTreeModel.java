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
package com.nabla.dc.client.model.fixed_asset;


import com.nabla.dc.shared.command.fixed_asset.FetchCompanyFixedAssetCategoryTree;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.HeterogeneousTreeModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IntegerField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.TreeStringIdField;
import com.nabla.wapp.client.model.field.TreeStringParentIdField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;


public class CompanyFixedAssetCategoryTreeModel extends HeterogeneousTreeModel<CompanyFixedAssetCategoryRecord> {

	static public class Fields {
		public String name() { return IFixedAssetCategory.NAME; }
		public String active() {return IFixedAssetCategory.ACTIVE; }
	}

	private static final Fields	fields = new Fields();
	protected final Integer		companyId;

	public CompanyFixedAssetCategoryTreeModel(final Integer companyId) {
		this.companyId = companyId;

		setFields(
			new TreeStringIdField(),
			new TreeStringParentIdField(),
			new TextField(fields.name(), FieldAttributes.READ_ONLY),
			new BooleanField(fields.active(), FieldAttributes.OPTIONAL),
			new IntegerField("iid", FieldAttributes.HIDDEN)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyFixedAssetCategoryTree(companyId/*, getParentId(request)*/);
	}

}
