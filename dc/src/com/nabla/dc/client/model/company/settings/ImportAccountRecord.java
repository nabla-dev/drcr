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

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.company.settings.IImportAccount;
import com.nabla.wapp.client.model.BasicRecord;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class ImportAccountRecord extends BasicRecord implements IImportAccount {

	public static final IRecordFactory<ImportAccountRecord>	factory = new IRecordFactory<ImportAccountRecord>() {
		@Override
		public ImportAccountRecord get(final JavaScriptObject data) {
			return new ImportAccountRecord(data);
		}
	};

	public ImportAccountRecord(final Record impl) {
		super(impl);
	}

	public ImportAccountRecord(final JavaScriptObject js) {
		super(js);
	}

	public Integer getFileId() {
		return getAttributeAsInt(IImportAccount.FILE);
	}

	public Boolean isRowHeader() {
		return getAttributeAsBoolean(IImportAccount.ROW_HEADER);
	}
	
	public SqlInsertOptions getOverwrite() {
		return SqlInsertOptions.valueOf(getAttributeAsString(IImportAccount.OVERWRITE));
	}

}
