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
package com.nabla.dc.client.model.fixed_asset;


import com.nabla.dc.shared.command.fixed_asset.FetchTransactionList;
import com.nabla.dc.shared.model.fixed_asset.ITransaction;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.nabla.wapp.client.model.field.PoundField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class TransactionListModel extends CModel<ListGridRecord> {

	static public class Fields {
		public String period() { return ITransaction.PERIOD; }
		public String clazz() { return ITransaction.CLASS; }
		public String type() { return ITransaction.TYPE; }
		public String amount() { return ITransaction.AMOUNT; }
		public String depreciationPeriod() { return ITransaction.DEPRECIATION_PERIOD; }
	}

	private static final Fields	fields = new Fields();
	private final Integer			assetId;

	public TransactionListModel(final Integer assetId) {
		super();

		this.assetId = assetId;
		setFields(
			new IdField(),

			new TextField(fields.period(), FieldAttributes.REQUIRED),
			new TextField(fields.clazz(), FieldAttributes.REQUIRED),
			new TextField(fields.type(), FieldAttributes.REQUIRED),
			new PoundField(fields.amount(), FieldAttributes.REQUIRED),
			new PositiveIntegerField(fields.depreciationPeriod(), FieldAttributes.OPTIONAL)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchTransactionList(assetId);
	}

}
