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
import com.nabla.dc.shared.model.fixed_asset.CompanyFixedAssetCategoryTree;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.ValueStoreWrapper;
import com.smartgwt.client.data.Record;


public class CompanyFixedAssetCategoryFormRecord extends Record {

	public static final IRecordFactory<CompanyFixedAssetCategoryFormRecord>	factory = new IRecordFactory<CompanyFixedAssetCategoryFormRecord>() {

		@Override
		public CompanyFixedAssetCategoryFormRecord get(JavaScriptObject data) {
			return new CompanyFixedAssetCategoryFormRecord(data);
		}

	};

	public CompanyFixedAssetCategoryFormRecord(JavaScriptObject js) {
		super(js);
	}

	public CompanyFixedAssetCategoryTree getCategories() {
		return getValue(CompanyFixedAssetCategoryFormModel.Fields.categories());
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(final String name) {
		return ((ValueStoreWrapper<T>)getAttributeAsRecord(name)).getData();
	}
}
