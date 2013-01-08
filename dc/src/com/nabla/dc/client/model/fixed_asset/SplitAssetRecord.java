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
import com.nabla.dc.shared.model.fixed_asset.ISplitAsset;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.IWizardRecord;
import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.smartgwt.client.data.Record;


public class SplitAssetRecord extends BasicListGridRecord implements ISplitAsset, IWizardRecord {

	public static final IRecordFactory<SplitAssetRecord>	factory = new IRecordFactory<SplitAssetRecord>() {
		@Override
		public SplitAssetRecord get(final JavaScriptObject data) {
			return new SplitAssetRecord(data);
		}
	};

	public SplitAssetRecord(final Record impl) {
		super(impl);
	}

	public SplitAssetRecord(final JavaScriptObject js) {
		super(js);
	}

	public String getNameA() {
		return getAttributeAsString(NAME_A);
	}

	public String getNameB() {
		return getAttributeAsString(NAME_B);
	}

	public String getReferenceA() {
		return getAttributeAsString(REFERENCE_A);
	}

	public String getReferenceB() {
		return getAttributeAsString(REFERENCE_B);
	}

	public Integer getCostA() {
		return getAttributeAsInt(COST_A);
	}

	public Integer getTotal() {
		return getAttributeAsInt(TOTAL);
	}

	public Integer getIdB() {
		return getAttributeAsInt(ID_B);
	}

	@Override
	public boolean getSuccess() {
		return true;
	}
}
