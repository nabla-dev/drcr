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
package com.nabla.dc.client.model.general;


import com.nabla.dc.shared.command.general.AddTaxRate;
import com.nabla.dc.shared.command.general.FetchTaxRateList;
import com.nabla.dc.shared.command.general.RemoveTaxRate;
import com.nabla.dc.shared.command.general.UpdateTaxRate;
import com.nabla.dc.shared.model.ITaxRate;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.CurrencyField;
import com.nabla.wapp.client.model.field.DeletedRecordField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IRecordAction;
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
		public String rate() { return ITaxRate.RATE; }
	}

	private static final Fields	fields = new Fields();

	public TaxRateListModel() {
		super(TaxRateRecord.factory);

		setFields(
			new DeletedRecordField(),
			new IdField(),
			new TextField(ITaxRate.NAME, ITaxRate.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new CurrencyField(ITaxRate.RATE, ITaxRate.RATE_CONSTRAINT),
			new BooleanField(ITaxRate.ACTIVE)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveTaxRate();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchTaxRateList();
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final TaxRateRecord record) {
		return new AddTaxRate(record.getName(), record.getRate(), record.getActive());
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final TaxRateRecord record) {
		return new UpdateTaxRate(record.getId(), record.getName(), record.getRate(), record.getActive());
	}

}
