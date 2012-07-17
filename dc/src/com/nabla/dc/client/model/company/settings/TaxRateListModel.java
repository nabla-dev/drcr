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
package com.nabla.dc.client.model.company.settings;


import com.nabla.dc.client.model.settings.TaxRateRecord;
import com.nabla.dc.shared.command.company.settings.FetchCompanyTaxRateList;
import com.nabla.dc.shared.command.company.settings.UpdateCompanyTaxRate;
import com.nabla.dc.shared.model.ITaxRate;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class TaxRateListModel extends CModel<TaxRateRecord> {

	static public class Fields {
		public String name() { return ITaxRate.NAME; }
		public String active() { return ITaxRate.ACTIVE; }
	}

	private static final Fields	fields = new Fields();
	private final Integer		companyId;

	public TaxRateListModel(final Integer companyId) {
		super(TaxRateRecord.factory);

		this.companyId = companyId;
		setFields(
			new IdField(),
			new TextField(fields.name(), FieldAttributes.READ_ONLY),
			new BooleanField(fields.active())
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyTaxRateList(companyId);
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final TaxRateRecord record) {
		return new UpdateCompanyTaxRate(companyId, record.getId(), record.getActive());
	}

}
