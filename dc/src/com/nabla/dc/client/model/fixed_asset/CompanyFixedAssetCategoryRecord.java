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

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.HeterogeneousTreeGridRecord;
import com.smartgwt.client.data.Record;


public class CompanyFixedAssetCategoryRecord extends HeterogeneousTreeGridRecord {

	public static final IRecordFactory<CompanyFixedAssetCategoryRecord>	factory = new IRecordFactory<CompanyFixedAssetCategoryRecord>() {

		@Override
		public CompanyFixedAssetCategoryRecord get(JavaScriptObject data) {
			return new CompanyFixedAssetCategoryRecord(data);
		}

	};

	public CompanyFixedAssetCategoryRecord(JavaScriptObject js) {
		super(js);
	}

	public CompanyFixedAssetCategoryRecord(Record record) {
		super(record);
	}

	public CompanyFixedAssetCategoryRecord(final CompanyFixedAssetCategoryRecord parent, final FixedAssetCategoryRecord category) {
		Assert.argumentNotNull(parent);
		Assert.argumentNotNull(category);

		setIsFolder(false);
		setParentStringId(parent.getStringId());
		setStringId("c" + category.getId());
		setName(category.getName());
		setActive(true);
		setSpeudoId(category.getId());	// store for later reuse
	}

	public CompanyFixedAssetCategoryRecord(final CompanyFixedAssetCategoryRecord parent, final CompanyFixedAssetCategoryRecord category) {
		Assert.argumentNotNull(parent);
		Assert.argumentNotNull(category);

		setIsFolder(false);
		setParentStringId(parent.getStringId());
		setStringId(category.getStringId());
		setName(category.getName());
		setActive(category.getActive());
		setSpeudoId(category.getSpeudoId());	// store for later reuse
	}

	@Override
	public String getName() {
		return getAttributeAsString(IFixedAssetCategory.NAME);
	}

	@Override
	public void setName(String value) {
		setAttribute(IFixedAssetCategory.NAME, value);
	}

	public Boolean getActive() {
		return getBoolean(IFixedAssetCategory.ACTIVE);
	}

	public void setActive(Boolean active) {
		setAttribute(IFixedAssetCategory.ACTIVE, active);
	}

	public Integer getSpeudoId() {
		return getAttributeAsInt(IFinancialStatementCategory.SPEUDO_ID);
	}

	public void setSpeudoId(Integer value) {
		setAttribute(IFinancialStatementCategory.SPEUDO_ID, value);
	}

}
