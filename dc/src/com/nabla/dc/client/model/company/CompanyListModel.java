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
package com.nabla.dc.client.model.company;


import com.nabla.dc.shared.command.company.FetchCompanyList;
import com.nabla.dc.shared.command.company.RemoveCompany;
import com.nabla.dc.shared.command.company.UpdateCompany;
import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.DeletedRecordField;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class CompanyListModel extends CModel<CompanyRecord> {

	static public class Fields {
		public String name() { return ICompany.NAME; }
		public String active() { return ICompany.ACTIVE; }
	}

	private static final Fields	fields = new Fields();

	public CompanyListModel() {
		super(CompanyRecord.factory);

		setFields(
			new DeletedRecordField(),
			new IdField(),
			new TextField(fields.name(), ICompany.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new BooleanField(fields.active())
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveCompany();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyList();
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final CompanyRecord record) {
		return new UpdateCompany(record.getId(), record.getName(), record.getActive());
	}

}
