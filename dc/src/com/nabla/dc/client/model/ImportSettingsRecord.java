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
package com.nabla.dc.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.IImportSettings;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.IWizardRecord;
import com.nabla.wapp.client.model.data.BasicRecord;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class ImportSettingsRecord extends BasicRecord implements IImportSettings, IWizardRecord {

	public static final IRecordFactory<ImportSettingsRecord>	factory = new IRecordFactory<ImportSettingsRecord>() {
		@Override
		public ImportSettingsRecord get(final JavaScriptObject data) {
			return new ImportSettingsRecord(data);
		}
	};

	public ImportSettingsRecord(final Record impl) {
		super(impl);
	}

	public ImportSettingsRecord(final JavaScriptObject js) {
		super(js);
	}

	public Integer getFileId() {
		return getAttributeAsInt(FILE);
	}

	public SqlInsertOptions getOverwrite() {
		return SqlInsertOptions.valueOf(getAttributeAsString(OVERWRITE));
	}

	@Override
	public boolean getSuccess() {
		return getBoolean(SUCCESS);
	}
}
