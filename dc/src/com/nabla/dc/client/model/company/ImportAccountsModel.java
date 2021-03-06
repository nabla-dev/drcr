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
package com.nabla.dc.client.model.company;

import com.nabla.dc.shared.command.company.ImportAccountList;
import com.nabla.dc.shared.model.company.IImportAccounts;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;


public class ImportAccountsModel extends CModel<ImportAccountsRecord> {

	static public class Fields {
		public String file() { return IImportAccounts.FILE; }
		public String rowHeader() { return IImportAccounts.ROW_HEADER; }
		public String overwrite() { return IImportAccounts.OVERWRITE; }
	}

	private static final Fields	fields = new Fields();
	private final Integer			companyId;

	public ImportAccountsModel(final Integer companyId) {
		super(ImportAccountsRecord.factory);

		this.companyId = companyId;
		setFields(
			new UploadFileField(fields.file(), FieldAttributes.REQUIRED),
			new BooleanField(fields.rowHeader(), FieldAttributes.REQUIRED),
			new TextField(fields.overwrite(), FieldAttributes.REQUIRED)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final ImportAccountsRecord record) {
		return new ImportAccountList(companyId, record.getFileId(), record.isRowHeader(), record.getOverwrite());
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final ImportAccountsRecord record) {
		return getAddCommand(record);
	}
}
