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
package com.nabla.dc.shared.command.fixed_asset;

import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;




public class ImportCompanyAssets implements IRecordAction<StringResult> {

	private int				companyId;
	private int				fileId;
	private SqlInsertOptions	overwrite;

	ImportCompanyAssets() {}	// for serialization only

	public ImportCompanyAssets(final int companyId, final int fileId, final SqlInsertOptions overwrite) {
		this.companyId = companyId;
		this.fileId = fileId;
		this.overwrite = overwrite;
	}

	public int getCompanyId() {
		return companyId;
	}

	public int getFileId() {
		return fileId;
	}

	public SqlInsertOptions getOverwrite() {
		return overwrite;
	}

	@Override
	public boolean validate(@SuppressWarnings("unused") IErrorList<Void> errors) throws DispatchException {
		return true;
	}
}
