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
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=ICompany.TABLE)
public class UpdateCompany implements IRecordAction<StringResult>, ICompany {

	@IRecordField(id=true)
	Integer				id;
	@IRecordField(unique=true)
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField
	Boolean				active;

	protected UpdateCompany() {}	// for serialization only

	public UpdateCompany(final Integer id, final String name, final Boolean active) {
		this.id = id;
		this.name = name;
		this.active = active;
	}

	@Override
	public boolean validate(final IErrorList errors) throws DispatchException {
		if (name == null)
			return true;
		if (!NAME_CONSTRAINT.validate(NAME, name, errors))
			return false;
		uname = name.toUpperCase();
		return true;
	}

}
