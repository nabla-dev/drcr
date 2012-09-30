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

import com.nabla.dc.shared.command.fixed_asset.FetchAssetProperties;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.IntegerField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class ViewAssetModel extends BasicAssetModel {

	private final int	assetId;

	public ViewAssetModel(final int assetId) {
		super();
		this.assetId = assetId;

		setFields(
			new IdField(),

			new TextField(fields.name(), FieldAttributes.READ_ONLY),
			new TextField(fields.category(), FieldAttributes.READ_ONLY),
			new TextField(fields.reference(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new TextField(fields.location(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),

			new DateField(fields.acquisitionDate(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new TextField(fields.acquisitionType(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.cost(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new TextField(fields.pi(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.initialAccumDep(), FieldAttributes.OPTIONAL),
			new IntegerField(fields.initialDepPeriod(), FieldAttributes.OPTIONAL),

			new IntegerField(fields.depPeriod(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.residualValue(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),

			new PositiveIntegerField(fields.openingAccumDep(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new IntegerField(fields.openingDepPeriod(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),

			new DateField(fields.disposalDate(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new TextField(fields.disposalType(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL),
			new PositiveIntegerField(fields.proceeds(), FieldAttributes.READ_ONLY, FieldAttributes.OPTIONAL)
		);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchAssetProperties(assetId);
	}

}
