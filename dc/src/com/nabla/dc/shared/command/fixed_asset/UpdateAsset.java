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
package com.nabla.dc.shared.command.fixed_asset;

import java.util.Date;

import com.nabla.dc.shared.model.fixed_asset.AcquisitionTypes;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.general.Nullable;

/**
 * @author nabla
 *
 */
public class UpdateAsset extends AddAsset {

	@IRecordField(id=true)
	int		id;

	UpdateAsset() {}

	public UpdateAsset(int id, final Integer companyId, final String name, final Integer companyAssetCategoryId,
			@Nullable final String reference, @Nullable final String location,
			final Date acquisitionDate, final AcquisitionTypes acquisitionType, int cost, @Nullable final String pi,
			int depreciationPeriod, final Integer residualValue,
			boolean createTransactions) {
		super(companyId, name, companyAssetCategoryId, reference, location, acquisitionDate, acquisitionType, cost, pi, depreciationPeriod, residualValue, createTransactions);
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
