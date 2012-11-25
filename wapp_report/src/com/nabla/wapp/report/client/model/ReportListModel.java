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
import com.nabla.wapp.client.model.field.EnabledRecordField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.SelectBoxField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.command.FetchReportList;
import com.nabla.wapp.report.shared.command.RemoveReport;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class ReportListModel extends CModel<ReportRecord> {

	static public class Fields {
		public String name() { return IReport.NAME; }
		public String template() { return IReport.TEMPLATE; }
		public String permission() { return IReport.PERMISSION; }
		public String reportFile() { return IReport.REPORT_FILE; }
	}

	private static final Fields	fields = new Fields();

	public ReportListModel() {
		setFields(
			new EnabledRecordField(),
			new IdField(),
			new TextField(fields.name(), IReport.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.template(), FieldAttributes.READ_ONLY),
			new SelectBoxField(fields.permission(), new ReportPermissionComboBoxModel(), IdField.NAME, "name", FieldAttributes.REQUIRED),

			new UploadFileField(fields.reportFile(), FieldAttributes.REQUIRED)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveReport();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchReportList();
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(@SuppressWarnings("unused") final ReportRecord record) {
		return null;
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(@SuppressWarnings("unused") final ReportRecord record) {
		return null;
	}

}
