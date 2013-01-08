/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.shared.command;

import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.IReportTable;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;


@IRecordTable(name=IReportTable.TABLE)
public class UpdateReport implements IRecordAction<StringResult> {

	@IRecordField(id=true)
	int					id;
	@IRecordField @Nullable
	String				name;
	@IRecordField @Nullable
	String				category;
	@IRecordField @Nullable
	Integer				role_id;

	UpdateReport() {}	// for serialization only

	public UpdateReport(final int id, @Nullable final String name, @Nullable final String category, @Nullable final Integer role_id) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.role_id = role_id;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return IReport.NAME_CONSTRAINT.validate(IReport.NAME, name, errors, ValidatorContext.UPDATE);
	}

}
