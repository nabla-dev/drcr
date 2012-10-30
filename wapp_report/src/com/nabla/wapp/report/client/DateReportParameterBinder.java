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
package com.nabla.wapp.report.client;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.ui.form.Control;
import com.nabla.wapp.client.ui.form.Form;
import com.nabla.wapp.report.shared.DateReportParameterValue;
import com.nabla.wapp.report.shared.IReportParameterValue;
import com.nabla.wapp.report.shared.ReportParameter;
import com.smartgwt.client.data.DataSourceField;

/**
 * The <code></code> object is used to
 *
 */
public class DateReportParameterBinder implements IReportParameterBinder {

	private static final DateTimeFormat	dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd");

	private final ReportParameter		parameter;

	public DateReportParameterBinder(final ReportParameter parameter) {
		this.parameter = parameter;
	}

	@Override
	public boolean createModelField(List<DataSourceField> fields) {
		fields.add(new DateField(parameter.getName(), FieldAttributes.REQUIRED));
		return true;
	}

	@Override
	public boolean createFormItem(Form form) {
		final Control ctrl = new Control();
		ctrl.setName(parameter.getName());
		ctrl.setText(parameter.getDescription());
		form.add(ctrl);
		return true;
	}

	@Override
	public void getNeedDefaultValue(Set<String> parameterNames) {
		parameterNames.add(parameter.getName());
	}

	@Override
	public Map<String, String> getValue(Form form, List<IReportParameterValue> values) {
		final Date dt = (Date)form.getValue(parameter.getName());
		values.add(new DateReportParameterValue(parameter.getName(), dt, dateFormatter.format(dt)));
		return null;
	}

}
