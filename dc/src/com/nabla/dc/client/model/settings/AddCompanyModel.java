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
package com.nabla.dc.client.model.settings;


import com.nabla.dc.shared.command.settings.AddCompany;
import com.nabla.dc.shared.model.ICompany;
import com.nabla.dc.shared.model.IFinancialYear;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class AddCompanyModel extends CModel<AddCompanyRecord> {

	static public class Fields {
		public String name() { return ICompany.NAME; }
		public static String financialYear() { return "financial_year"; }
		public static String startDate() { return "start_date"; }
	}

	private static final Fields	fields = new Fields();

	@SuppressWarnings("static-access")
	public AddCompanyModel() {
		super(AddCompanyRecord.factory);

		setFields(
			new TextField(fields.name(), ICompany.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.financialYear(), IFinancialYear.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new DateField(fields.startDate(), FieldAttributes.REQUIRED),
			new IdField(),
			new BooleanField(ICompany.ACTIVE, FieldAttributes.OPTIONAL)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public IAction<StringResult> getAddCommand(final AddCompanyRecord r) {
		return new AddCompany(r.getName(), r.getFinancialYear(), r.getStartDate());
	}

}
