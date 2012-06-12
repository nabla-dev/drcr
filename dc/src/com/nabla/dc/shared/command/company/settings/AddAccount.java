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
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IAccount.TABLE)
public class AddAccount implements IRecordAction<StringResult>, IAccount {

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

	@Override
	public boolean validate(final IErrorList errors) {
		return doValidate(true, errors);
	}
	
	protected boolean doValidate(boolean add, final IErrorList errors) {
		int n = errors.size();
		if (add || code != null)
			CODE_CONSTRAINT.validate(CODE, code, errors);
		if (add || name != null) {
			if (NAME_CONSTRAINT.validate(NAME, name, errors))
				uname = name.toUpperCase();
		}
		if (cost_centre != null) {
			if (cost_centre.isEmpty())
				cost_centre = null;
			else
				CC_CONSTRAINT.validate(COST_CENTRE, cost_centre, errors);
		}
		if (department != null) {
			if (department.isEmpty())
				department = null;
			else
				DEP_CONSTRAINT.validate(DEPARTMENT, department, errors);
		}
		return n == errors.size();
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
