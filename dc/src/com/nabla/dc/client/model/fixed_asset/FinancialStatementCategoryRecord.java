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
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.smartgwt.client.data.Record;


public class FinancialStatementCategoryRecord extends BasicListGridRecord implements IFixedAssetCategory {

	public static final IRecordFactory<FinancialStatementCategoryRecord>	factory = new IRecordFactory<FinancialStatementCategoryRecord>() {

		@Override
		public FinancialStatementCategoryRecord get(JavaScriptObject data) {
			return new FinancialStatementCategoryRecord(data);
		}

	};

	public FinancialStatementCategoryRecord(Record impl) {
		super(impl);
	}

	public FinancialStatementCategoryRecord(JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

	public Boolean getActive() {
		return getBoolean(ACTIVE);
	}

}
