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


import com.nabla.dc.shared.command.company.FetchCompanyName;
import com.nabla.dc.shared.model.ICompany;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class FixedAssetCategoryFormModel extends CModel<FixedAssetCategoryRecord> {

	static public class Fields {
		public String name() { return ICompany.NAME; }
		public String availableCategories() { return "availableCategories"; }
		public String categories() { return "categories"; }
	}

	private static final Fields	fields = new Fields();
	protected final Integer		companyId;

	public FixedAssetCategoryFormModel(final Integer companyId) {
		super(FixedAssetCategoryRecord.factory);
		this.companyId = companyId;
	}

	public Fields fields() {
		return fields;
	}

	public AvailableFixedAssetCategoryTreeModel getAvailableTreeModel() {
		return new AvailableFixedAssetCategoryTreeModel(companyId);
	}

	public FixedAssetCategoryTreeModel getAssignedTreeModel() {
		return new FixedAssetCategoryTreeModel(companyId);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyName(companyId);
	}
/*
	@Override
	public IAction<StringResult> getUpdateCommand(final CompanyFixedAssetCategoryRecord record) {
		final SelectionDelta delta = record.getDefinitionDelta();
		if (delta == null || delta.isEmpty())
			return null;	// save a round trip to the server
		return new UpdateRoleDefinition(roleId, delta);
	}
*/
}
