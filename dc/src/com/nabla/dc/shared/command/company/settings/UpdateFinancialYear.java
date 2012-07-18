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
package com.nabla.dc.shared.command.company.settings;

import com.nabla.dc.shared.model.IFinancialYear;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IFinancialYear.TABLE)
public class UpdateFinancialYear implements IRecordAction<StringResult>, IFinancialYear {

	@IRecordField(id=true)
	Integer		id;
	@IRecordField(unique=true)
	String		name;

	protected UpdateFinancialYear() {}	// for serialization only

	public UpdateFinancialYear(final Integer id, final String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		return NAME_CONSTRAINT.validate(NAME, name, errors);
	}

	public Integer getId(){
		return id;
	}
}
