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
public class ChangeCompanyLogo implements IRecordAction<StringResult> {

	@IRecordField(name="id", id=true)
	Integer				companyId;
	Integer				fileId;
	@IRecordField
	transient Integer	logo_id;

	ChangeCompanyLogo() {}	// for serialization only

	public ChangeCompanyLogo(final Integer companyId, final Integer fileId) {
		this.companyId = companyId;
		this.fileId = fileId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setLogoId(final Integer id) {
		logo_id = id;
	}

	@Override
	public boolean validate(@SuppressWarnings("unused") IErrorList errors) throws DispatchException {
		return true;
	}

}
