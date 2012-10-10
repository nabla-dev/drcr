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

import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IFinancialStatementCategory.TABLE)
public class AddFinancialStatementCategory implements IRecordAction<StringResult>, IFinancialStatementCategory {

	@IRecordField(unique=true)
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField
	Boolean				active;

	AddFinancialStatementCategory() {}	// for serialization only

	public AddFinancialStatementCategory(final String name, final Boolean active) {
		this.name = name;
		this.active = active;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.ADD);
	}

	protected boolean doValidate(final IErrorList<Void> errors, final ValidatorContext ctx) throws DispatchException {
		if (name != null)
			uname = name.toUpperCase();
		return NAME_CONSTRAINT.validate(NAME, name, errors, ctx);
	}

}
