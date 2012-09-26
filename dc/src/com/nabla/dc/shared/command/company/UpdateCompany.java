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
package com.nabla.dc.shared.command.company;

import com.nabla.dc.shared.model.company.ICompany;
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
@IRecordTable(name=ICompany.TABLE)
public class UpdateCompany implements IRecordAction<StringResult>, ICompany {

	@IRecordField(id=true)
	int					id;
	@IRecordField(unique=true) @Nullable
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField @Nullable
	Boolean				active;

	UpdateCompany() {}	// for serialization only

	public UpdateCompany(final int id, @Nullable final String name, @Nullable final Boolean active) {
		this.id = id;
		this.name = name;
		this.active = active;
	}

	@Override
	public boolean validate(final IErrorList errors) throws DispatchException {
		if (name != null)
			uname = name.toUpperCase();
		return NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.UPDATE);
	}

}
