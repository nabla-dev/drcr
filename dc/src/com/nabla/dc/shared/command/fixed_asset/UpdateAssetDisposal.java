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
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAsset.TABLE)
public class UpdateAssetDisposal implements IRecordAction<StringResult> {

	@IRecordField(id=true)
	int				id;
	@IRecordField
	Date			disposal_date;
	@IRecordField
	DisposalTypes	disposal_type;
	@IRecordField
	Integer			proceeds;

	UpdateAssetDisposal() {}	// for serialization only

	public UpdateAssetDisposal(int id, final Date dt, final DisposalTypes type, int proceeds) {
		this.id = id;
		this.disposal_date = dt;
		this.disposal_type = type;
		this.proceeds = proceeds;
	}

	@Override
	public boolean validate(IErrorList errors) throws DispatchException {
		switch (disposal_type) {
		case SOLD:
			if (proceeds == null)
				proceeds = 0;
			else if (proceeds < 0) {
				errors.add(IAsset.PROCEEDS, CommonServerErrors.INVALID_VALUE);
				return false;
			}
			break;
		default:
			proceeds = 0;
			break;
		}
		return true;
	}

	public int getId() {
		return id;
	}

	public Date getDisposalDate() {
		return disposal_date;
	}

	public DisposalTypes getDisposalType() {
		return disposal_type;
	}

	public Integer getProceeds() {
		return proceeds;
	}

}
