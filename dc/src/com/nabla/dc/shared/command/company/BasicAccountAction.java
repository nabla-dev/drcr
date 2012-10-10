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

import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.shared.csv.ICsvField;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAccount.TABLE)
public class BasicAccountAction implements IAccount {

	@IRecordField(unique=true)
	@ICsvField
	String				code;
	@IRecordField
	@ICsvField(name="cc")
	String				cost_centre;
	@IRecordField
	@ICsvField(name="dep")
	String				department;
	@IRecordField(unique=true)
	@ICsvField
	String				name;
	@IRecordField
	transient String	uname;
	@IRecordField
	@ICsvField(name="bs")
	Boolean				balance_sheet;
	@IRecordField
	Boolean				active;

	BasicAccountAction() {}	// for serialization only

	public BasicAccountAction(final String code, final String name, final String cc, final String dep, final Boolean bs, final Boolean active) {
		this.code = code;
		this.name = name;
		this.cost_centre = cc;
		this.department = dep;
		this.balance_sheet = bs;
		this.active = active;
	}

	protected boolean doValidate(final IErrorList<Void> errors, final ValidatorContext ctx) throws DispatchException {
		int n = errors.size();
		CODE_CONSTRAINT.validate(CODE, code, errors, ctx);
		if (name != null)
			uname = name.toUpperCase();
		NAME_CONSTRAINT.validate(NAME, name, errors, ctx);
		if (cost_centre != null && cost_centre.isEmpty())
			cost_centre = null;
		COST_CENTRE_CONSTRAINT.validate(COST_CENTRE, cost_centre, errors, ctx);
		if (department != null && department.isEmpty())
			department = null;
		DEPARTMENT_CONSTRAINT.validate(DEPARTMENT, department, errors, ctx);
		return n == errors.size();
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
