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

import com.nabla.dc.shared.model.fixed_asset.DisposalTypes;
import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.dc.shared.model.fixed_asset.IAssetTable;
import com.nabla.dc.shared.model.fixed_asset.IDisposal;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAssetTable.TABLE)
public class UpdateAssetDisposal implements IRecordAction<StringResult>, IDisposal {

	@IRecordField(id=true)
	int				id;
	@IRecordField(name=IAssetTable.DISPOSAL_DATE)
	Date			disposal_date;
	@IRecordField(name=IAssetTable.DISPOSAL_TYPE)
	DisposalTypes	disposal_type;
	@IRecordField(name=IAssetTable.PROCEEDS) @Nullable
	Integer			proceeds;

	UpdateAssetDisposal() {}	// for serialization only

	public UpdateAssetDisposal(int id, final Date dt, final DisposalTypes type, @Nullable Integer proceeds) {
		this.id = id;
		this.disposal_date = dt;
		this.disposal_type = type;
		this.proceeds = proceeds;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return Validator.execute(this, null, errors);
	}

	public int getId() {
		return id;
	}

	@Override
	public @Nullable Integer getProceeds() {
		return proceeds;
	}

	@Override
	public Date getDate() {
		return disposal_date;
	}

	@Override
	public DisposalTypes getType() {
		return disposal_type;
	}

	@Override
	public void setProceeds(@Nullable Integer value) {
		proceeds = value;
	}

	@Override
	public String getProceedsField() {
		return IAsset.PROCEEDS;
	}

	@Override
	public String getDateField() {
		return IAsset.DISPOSAL_DATE;
	}

}
