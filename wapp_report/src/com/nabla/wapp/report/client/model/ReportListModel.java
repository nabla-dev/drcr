/**
* Copyright 2011 nabla
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


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.AbstractBasicModel;
import com.nabla.wapp.client.model.EnabledRecordField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.SelectBoxField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.command.AddReport;
import com.nabla.wapp.report.shared.command.FetchReportList;
import com.nabla.wapp.report.shared.command.RemoveReport;
import com.nabla.wapp.report.shared.command.UpdateReport;
import com.nabla.wapp.shared.dispatch.IAction;
import com.smartgwt.client.types.DSOperationType;

/**
 * @author nabla
 *
 */
@Singleton
public class ReportListModel extends AbstractBasicModel {

	static public class Fields {
		public String name() { return IReport.NAME; }
		public String template() { return IReport.TEMPLATE; }
		public String permission() { return IReport.PERMISSION; }
		public String reportFile() { return IReport.REPORT_FILE; }
	}

	@Inject
	public ReportListModel(final ReportPermissionComboBoxModel permissionsModel) {
		Assert.unique(ReportListModel.class);

		setFields(
			new EnabledRecordField(),
			new IdField(),
			new TextField(IReport.NAME, IReport.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(IReport.TEMPLATE, FieldAttributes.READ_ONLY),
			new SelectBoxField(IReport.PERMISSION, permissionsModel, IdField.NAME, "name", FieldAttributes.REQUIRED),

			new UploadFileField(IReport.REPORT_FILE, FieldAttributes.REQUIRED)
				);
	}

	@Override
	public IAction getCommand(final DSOperationType op) {
		switch (op) {
			case FETCH:
				return new FetchReportList();
			case REMOVE:
				return new RemoveReport();
			case UPDATE:
				return new UpdateReport();
			case ADD:
				return new AddReport();
			default:
				return null;
		}
	}

}
