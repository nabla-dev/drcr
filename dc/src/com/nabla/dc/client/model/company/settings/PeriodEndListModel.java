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

import com.nabla.dc.shared.command.company.settings.FetchPeriodEndList;
import com.nabla.dc.shared.command.company.settings.RemovePeriodEnd;
import com.nabla.dc.shared.model.IAccount;
import com.nabla.dc.shared.model.IPeriodEnd;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class PeriodEndListModel extends CModel<PeriodEndRecord> {

	static public class Fields {
		public String name() { return IPeriodEnd.NAME; }
		public String endDate() { return IPeriodEnd.END_DATE; }
		public String visible() { return IPeriodEnd.VISIBLE; }
		public String nl_opened() { return IPeriodEnd.NL_OPENED; }
		public String sl_opened() { return IPeriodEnd.SL_OPENED; }
		public String pl_opened() { return IPeriodEnd.PL_OPENED; }
		public String cb_opened() { return IPeriodEnd.CB_OPENED; }
	}

	private static final Fields	fields = new Fields();
	private final Integer			companyId;

	public PeriodEndListModel(final Integer companyId) {
		super(PeriodEndRecord.factory);

		this.companyId = companyId;
		setFields(
			new IdField(),
			new TextField(fields.name(), IAccount.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(fields.endDate(), IAccount.CC_CONSTRAINT),
			new BooleanField(fields.visible(), FieldAttributes.REQUIRED)
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchPeriodEndList(companyId);
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemovePeriodEnd();
	}
	/*
	@Override
	public IAction<StringResult> getUpdateCommand(final PeriodEndRecord record) {
		return new UpdatePeriodEnd(record.getId(), record.getCode(), record.getName(), record.getCostCentre(), record.getDepartment(), record.isBalanceSheet(), record.getActive());
	}

	@Override
	public IAction<StringResult> getAddCommand(final PeriodEndRecord record) {
		return new AddPeriodEnd(companyId, record.getCode(), record.getName(), record.getCostCentre(), record.getDepartment(), record.isBalanceSheet(), record.getActive());
	}*/
}
