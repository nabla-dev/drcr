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

import com.google.inject.Singleton;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.AbstractBasicModel;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.report.shared.command.FetchReportPermissionsComboBox;
import com.nabla.wapp.shared.model.AbstractOperationAction;
import com.smartgwt.client.types.DSOperationType;

/**
 * The <code>RoleComboBoxModel</code> object is used to display
 * list of roles in a ComboBox
 *
 */
@Singleton
public class ReportPermissionComboBoxModel extends AbstractBasicModel {

	public ReportPermissionComboBoxModel() {
		Assert.unique(ReportPermissionComboBoxModel.class);

		setFields(
			new IdField(),
			new TextField("name")
				);
	}

	@Override
	public AbstractOperationAction getCommand(final DSOperationType op) {
		return (op == DSOperationType.FETCH) ? new FetchReportPermissionsComboBox() : null;
	}

}
