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

import com.nabla.dc.shared.model.fixed_asset.IAsset;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAsset.TABLE)
public class UpdateAssetField implements IRecordAction<StringResult>, IAsset {

	@IRecordField(id=true)
	int			id;
	@IRecordField @Nullable
	String		name;
	@IRecordField @Nullable
	String		reference;
	@IRecordField @Nullable
	String		location;
	@IRecordField @Nullable
	String		purchase_invoice;
	@IRecordField @Nullable
	Integer		depreciation_period;

	UpdateAssetField() {}	// for serialization only

	public UpdateAssetField(int id, @Nullable final String name,
			@Nullable final String reference, @Nullable final String location,
			@Nullable final String pi, @Nullable Integer depreciationPeriod) {
		this.id = id;
		this.name = name;
		this.reference = reference;
		this.location = location;
		this.purchase_invoice = pi;
		this.depreciation_period = depreciationPeriod;
	}

	public UpdateAssetField(int id, @Nullable final String name, @Nullable final String reference) {
		this(id, name, reference, null, null, null);
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		int n = errors.size();

		NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.UPDATE);
		REFERENCE_CONSTRAINT.validate(REFERENCE, reference, errors, ValidatorContext.UPDATE);
		LOCATION_CONSTRAINT.validate(LOCATION, location, errors, ValidatorContext.UPDATE);
		PURCHASE_INVOICE_CONSTRAINT.validate(PURCHASE_INVOICE, purchase_invoice, errors, ValidatorContext.UPDATE);

		return n == errors.size();
	}

	public int getId() {
		return id;
	}

	public @Nullable Integer getDepreciationPeriod() {
		return depreciation_period;
	}

}
