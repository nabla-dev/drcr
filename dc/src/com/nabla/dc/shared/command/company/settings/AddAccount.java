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

import com.nabla.dc.shared.model.IAccount;
import com.nabla.wapp.shared.csv.ICsvField;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAccount.TABLE)
public class AddAccount implements IAction<StringResult>, IAccount {

	private static final long serialVersionUID = 1L;

	@IRecordField
	Integer				company_id;
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

	public AddAccount() {}	// for serialization only

	public AddAccount(final Integer companyId, final String code, final String name, final String cc, final String dep, final Boolean bs, final Boolean active) {
		this.company_id = companyId;
		this.code = code;
		this.name = name;
		this.cost_centre = cc;
		this.department = dep;
		this.balance_sheet = bs;
		this.active = active;
	}

	public void validate() throws ValidationException {
		doValidate(true);
	}

	public void validate(final IErrorList errors) {
		
	}
	
	protected void doValidate(final boolean add) throws ValidationException {
		if (add || code != null)
			CODE_CONSTRAINT.validate(CODE, code);
		if (add || name != null) {
			NAME_CONSTRAINT.validate(NAME, name);
			uname = name.toUpperCase();
		}
		if (cost_centre != null) {
			if (cost_centre.isEmpty())
				cost_centre = null;
			else
				CC_CONSTRAINT.validate(COST_CENTRE, cost_centre);
		}
		if (department != null) {
			if (department.isEmpty())
				department = null;
			else
				DEP_CONSTRAINT.validate(DEPARTMENT, department);
		}
	}

	public Integer getCompanyId() {
		return company_id;
	}

	public void setCompanyId(Integer company_id) {
		this.company_id = company_id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
