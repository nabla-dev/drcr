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

import com.nabla.dc.client.ui.Resource;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.wapp.client.model.field.EnumField;
import com.nabla.wapp.client.model.field.FieldAttributes;


public class AssetCategoryTypeField extends EnumField {

	public AssetCategoryTypeField(final String name) {
		super(name, FixedAssetCategoryTypes.values(), Resource.strings.fixedAssetCategoryTypes(), FieldAttributes.REQUIRED);
		this.setDefaultValue(FixedAssetCategoryTypes.TANGIBLE);
	}
}
