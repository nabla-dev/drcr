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

import java.util.Date;

import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.dc.shared.model.company.IFinancialYear;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;


public class AddCompany implements IRecordAction<StringResult> {

	String	name;
	String	financialYear;
	Date	startDate;

	AddCompany() {}	// for serialization only

	public AddCompany(final String name, final String financialYear, final Date startDate) {
		this.name = name;
		this.financialYear = financialYear;
		this.startDate = startDate;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		int n = errors.size();
		ICompany.NAME_CONSTRAINT.validate(ICompany.NAME, name, errors, ValidatorContext.ADD);
		IFinancialYear.NAME_CONSTRAINT.validate(IFinancialYear.NAME, financialYear, errors, ValidatorContext.ADD);
		if (startDate == null)
			errors.add("start_date", CommonServerErrors.REQUIRED_VALUE);
		return n == errors.size();
	}

	public String getName() {
		return name;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public Date getStartDate() {
		return startDate;
	}

}
