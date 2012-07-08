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

import java.util.Date;

import com.nabla.dc.shared.model.ICompany;
import com.nabla.dc.shared.model.IFinancialYear;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
public class AddCompany implements IRecordAction<StringResult> {

	String	name;
	String	financialYear;
	Date	startDate;

	protected AddCompany() {}	// for serialization only

	public AddCompany(final String name, final String financialYear, final Date startDate) {
		this.name = name;
		this.financialYear = financialYear;
		this.startDate = startDate;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		int n = errors.size();
		ICompany.NAME_CONSTRAINT.validate(ICompany.NAME, name, errors);
		IFinancialYear.NAME_CONSTRAINT.validate(IFinancialYear.NAME, financialYear, errors);
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
