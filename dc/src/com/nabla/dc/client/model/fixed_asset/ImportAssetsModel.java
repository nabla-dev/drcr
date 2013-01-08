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
package com.nabla.dc.client.model.fixed_asset;

import com.nabla.dc.shared.command.ImportAssets;
import com.nabla.dc.shared.model.fixed_asset.IImportAssets;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;


public class ImportAssetsModel extends CModel<ImportAssetsRecord> {

	static public class Fields implements IImportAssets {
		public String file() { return FILE_ID; }
		public String overwrite() { return OVERWRITE; }
	}

	private static final Fields	fields = new Fields();

	public ImportAssetsModel() {
		super(ImportAssetsRecord.factory);

		setFields(
			new UploadFileField(fields.file(), FieldAttributes.REQUIRED),
			new TextField(fields.overwrite(), FieldAttributes.REQUIRED)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final ImportAssetsRecord record) {
		return new ImportAssets(record.getFileId(), record.getOverwrite());
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final ImportAssetsRecord record) {
		return getAddCommand(record);
	}
}
