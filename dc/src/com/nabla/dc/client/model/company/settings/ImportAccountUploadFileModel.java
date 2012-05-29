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
package com.nabla.dc.client.model.company.settings;

import com.nabla.dc.shared.command.company.settings.ImportAccountList;
import com.nabla.dc.shared.model.company.settings.IImportAccount;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class ImportAccountUploadFileModel extends CModel<ImportAccountRecord> {

	static public class Fields {
		public String file() { return IImportAccount.FILE; }
		public String rowHeader() { return IImportAccount.ROW_HEADER; }
		public String overwrite() { return IImportAccount.OVERWRITE; }
	}

	private static final Fields	fields = new Fields();
	private final Integer		companyId;
	
	public ImportAccountUploadFileModel(final Integer companyId) {
		super(ImportAccountRecord.factory);
		
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
	public IAction<StringResult> getAddCommand(final ImportAccountRecord record) {
		return new ImportAccountList(companyId, record.isRowHeader(), record.getOverwrite());
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final ImportAccountRecord record) {
		return getAddCommand(record);
	}
}
