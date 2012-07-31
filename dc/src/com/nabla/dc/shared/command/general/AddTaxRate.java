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
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=ITaxRate.TABLE)
public class AddTaxRate implements IRecordAction<StringResult>, ITaxRate {

	@IRecordField(unique=true)
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField
	Integer				rate;
	@IRecordField
	Boolean				active;

	protected AddTaxRate() {}	// for serialization only

	public AddTaxRate(final String name, final Integer rate, final Boolean active) {
		this.name = name;
		this.rate = rate;
		this.active = active;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		int n = errors.size();
		if (NAME_CONSTRAINT.validate(NAME, name, errors))
			uname = name.toUpperCase();
		if (rate != null)
			RATE_CONSTRAINT.validate(RATE, rate, ServerErrors.INVALID_TAX_CODE_RATE, errors);
		return n == errors.size();
	}

}