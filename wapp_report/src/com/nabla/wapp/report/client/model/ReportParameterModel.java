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

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nabla.wapp.client.model.AbstractBasicModel;
import com.nabla.wapp.report.client.IReportParameterBinder;
import com.nabla.wapp.shared.model.AbstractOperationAction;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.DSOperationType;

/**
 * The <code></code> object is used to
 *
 */
public class ReportParameterModel extends AbstractBasicModel {

	@Inject
	public ReportParameterModel(@Assisted final List<IReportParameterBinder> parameters) {
		final List<DataSourceField> fields = new LinkedList<DataSourceField>();
		for (IReportParameterBinder parameter : parameters)
			parameter.createModelField(fields);
		setFields(fields.toArray(new DataSourceField[0]));
	}

	@Override
	public AbstractOperationAction getCommand(@SuppressWarnings("unused") DSOperationType op) {
		return null;
	}

}
