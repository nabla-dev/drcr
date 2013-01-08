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

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.BasicRecord;
import com.nabla.wapp.report.shared.IReport;
import com.smartgwt.client.data.Record;

public class AddReportRecord extends BasicRecord {

	public static final IRecordFactory<AddReportRecord>	factory = new IRecordFactory<AddReportRecord>() {

		@Override
		public AddReportRecord get(JavaScriptObject data) {
			return new AddReportRecord(data);
		}

	};

	public AddReportRecord(Record impl) {
		super(impl);
	}

	public AddReportRecord(JavaScriptObject js) {
		super(js);
	}

	public Integer getFileId() {
		return getAttributeAsInt(IReport.REPORT_FILE);
	}

}
