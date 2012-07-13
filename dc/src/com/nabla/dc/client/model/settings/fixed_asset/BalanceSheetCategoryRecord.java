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
package com.nabla.dc.client.model.settings.fixed_asset;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.client.model.BasicListGridRecord;
import com.nabla.wapp.client.model.IRecordFactory;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class BalanceSheetCategoryRecord extends BasicListGridRecord implements IFixedAssetCategory {

	public static final IRecordFactory<BalanceSheetCategoryRecord>	factory = new IRecordFactory<BalanceSheetCategoryRecord>() {

		@Override
		public BalanceSheetCategoryRecord get(JavaScriptObject data) {
			return new BalanceSheetCategoryRecord(data);
		}

	};

	public BalanceSheetCategoryRecord(Record impl) {
		super(impl);
	}

	public BalanceSheetCategoryRecord(JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

	public Boolean getActive() {
		return getBoolean(ACTIVE);
	}

}
