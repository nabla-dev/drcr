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
package com.nabla.wapp.report.client.model;

import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.command.AddReport;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;

public class AddReportModel extends CModel<AddReportRecord> {

	static public class Fields {
		public String file() { return IReport.REPORT_FILE; }
	}

	private static final Fields	fields = new Fields();

	public Fields fields() {
		return fields;
	}

	public AddReportModel() {
		super(AddReportRecord.factory);

		setFields(
			new UploadFileField(fields.file(), FieldAttributes.REQUIRED)
				);
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final AddReportRecord record) {
		return new AddReport(record.getFileId());
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final AddReportRecord record) {
		return getAddCommand(record);
	}

}
