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


import com.nabla.dc.shared.command.fixed_asset.FetchCompanyFixedAssetCategoryList;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.Record;


public class CompanyFixedAssetCategoryListModel extends CModel<Record> {

	private final Integer	companyId;

	public CompanyFixedAssetCategoryListModel(final Integer companyId) {
		this.companyId = companyId;

		setFields(
			new IdField(),
			new TextField(IFixedAssetCategory.NAME)
				);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyFixedAssetCategoryList(companyId);
	}

}
