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
package com.nabla.dc.shared.command.settings;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.ITaxRate;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
@IRecordTable(name=ITaxRate.TABLE)
public class AddTaxRate implements IAction<StringResult>, ITaxRate {

	private static final long serialVersionUID = 1L;

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

	public void validate() throws ValidationException {
		NAME_CONSTRAINT.validate(NAME, name);
		uname = name.toUpperCase();
		if (rate != null)
			RATE_CONSTRAINT.validate(RATE, rate, ServerErrors.INVALID_TAX_CODE_RATE);
	}

}
