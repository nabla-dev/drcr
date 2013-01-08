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


import com.nabla.dc.shared.command.company.FetchCompanyName;
import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.Record;


public class TaxRateFormModel extends CModel<Record> {

	static public class Fields {
		public String name() { return ICompany.NAME; }
	}

	private static final Fields	fields = new Fields();
	private final Integer		companyId;

	public TaxRateFormModel(final Integer companyId) {
		super();

		this.companyId = companyId;
		setFields(
			new TextField(fields.name(), FieldAttributes.READ_ONLY)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyName(companyId);
	}

}
