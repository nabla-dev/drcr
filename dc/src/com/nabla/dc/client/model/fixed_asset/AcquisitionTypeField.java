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
import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.wapp.client.model.field.EnumField;
import com.nabla.wapp.client.model.field.FieldAttributes;

/**
 * @author nabla
 *
 */
public class AcquisitionTypeField extends EnumField {

	public AcquisitionTypeField(final String name, final FieldAttributes... attributes) {
		super(name, AcquisitionTypes.values(), Resource.strings.fixedAssetAcquisitionTypes(), attributes);
		this.setDefaultValue(AcquisitionTypes.PURCHASE);
	}
}
