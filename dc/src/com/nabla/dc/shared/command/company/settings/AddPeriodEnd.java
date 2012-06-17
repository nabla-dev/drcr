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

import java.sql.Date;

import com.nabla.dc.shared.model.IPeriodEnd;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
@IRecordTable(name=IPeriodEnd.TABLE)
public class AddPeriodEnd implements IRecordAction<StringResult>, IPeriodEnd {

	private static final long serialVersionUID = 1L;

	@IRecordField
	Integer			company_id;
	@IRecordField(unique=true)
	String			name;
	@IRecordField
	Date			end_date;
	@IRecordField
	Boolean			visible;

	public AddPeriodEnd() {}	// for serialization only

	public AddPeriodEnd(final Integer companyId, final String name, final Date endDate, final Boolean visible) {
		this.company_id = companyId;
		this.name = name;
		this.end_date = endDate;
		this.visible = visible;
	}

	@Override
	public boolean validate(final IErrorList errors) {
		return doValidate(true, errors);
	}

	protected boolean doValidate(boolean add, final IErrorList errors) {
		int n = errors.size();
		if (add || name != null)
			NAME_CONSTRAINT.validate(NAME, name, errors);
		if (add && end_date == null)
			errors.add(END_DATE, CommonServerErrors.REQUIRED_VALUE);
		return n == errors.size();
	}

}
