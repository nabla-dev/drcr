/**
* Copyright 2010 nabla
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
package com.nabla.fixed_assets.client.model;


import com.nabla.fixed_assets.shared.command.FetchAssetAcquisition;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.client.model.AbstractBasicModel;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.model.AbstractOperationAction;
import com.smartgwt.client.types.DSOperationType;

/**
 * @author nabla
 *
 */
public class AssetAcquisitionModel extends AbstractBasicModel {

	static public class Fields {
		public String acquisitionDate() { return IAsset.ACQUISITION_DATE; }
		public String acquisitionType() { return IAsset.ACQUISITION_TYPE; }
		public String cost() { return IAsset.COST; }
		public String pi() { return IAsset.PI; }
	}

	public AssetAcquisitionModel() {
		setFields(
			new IdField(),
			new DateField(IAsset.ACQUISITION_DATE, FieldAttributes.REQUIRED),
			new AcquisitionTypeField(IAsset.ACQUISITION_TYPE, FieldAttributes.REQUIRED),
			new PositiveIntegerField(IAsset.COST, FieldAttributes.REQUIRED),
			new TextField(IAsset.PI, IAsset.PI_CONSTRAINT, FieldAttributes.OPTIONAL)
				);
	}

	@Override
	public AbstractOperationAction getCommand(final DSOperationType op) {
		return (op == DSOperationType.FETCH) ? new FetchAssetAcquisition() : null;
	}

}
