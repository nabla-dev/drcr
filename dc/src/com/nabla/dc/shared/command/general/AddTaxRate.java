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
package com.nabla.dc.shared.command.general;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.ITaxRate;
import com.nabla.dc.shared.model.ITaxRateTable;
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
@IRecordTable(name=ITaxRateTable.TABLE)
public class AddTaxRate implements IRecordAction<StringResult> {

	@IRecordField(name=ITaxRateTable.NAME,unique=true) @Nullable
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField(name=ITaxRateTable.RATE) @Nullable
	Integer				rate;
	@IRecordField(name=ITaxRateTable.ACTIVE) @Nullable
	Boolean				active;

	AddTaxRate() {}	// for serialization only

	public AddTaxRate(@Nullable final String name, @Nullable final Integer rate, @Nullable final Boolean active) {
		this.name = name;
		this.rate = rate;
		this.active = active;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.ADD);
	}

	protected boolean doValidate(final IErrorList<Void> errors, final ValidatorContext ctx) throws DispatchException {
		int n = errors.size();
		if (name != null)
			uname = name.toUpperCase();
		ITaxRate.NAME_CONSTRAINT.validate(ITaxRate.NAME, name, errors, ctx);
		ITaxRate.RATE_CONSTRAINT.validate(ITaxRate.RATE, rate, ServerErrors.INVALID_TAX_CODE_RATE, errors, ValidatorContext.UPDATE);
		return n == errors.size();
	}

}
