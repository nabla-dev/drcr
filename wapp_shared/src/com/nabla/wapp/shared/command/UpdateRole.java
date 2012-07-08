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
package com.nabla.wapp.shared.command;

import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name="role")
public class UpdateRole extends AddRole {

	@IRecordField(id=true)
	Integer				id;
	@IRecordField
	transient String	uname;

	protected UpdateRole() {}	// for serialization only

	public UpdateRole(final Integer id, final String name) {
		super(name);
		this.id = id;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		if (!super.validate(errors))
			return false;
		uname = name.toUpperCase();
		return true;
	}

}
