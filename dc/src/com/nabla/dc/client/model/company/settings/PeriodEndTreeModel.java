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


import com.nabla.dc.shared.command.company.settings.FetchPeriodEndTree;
import com.nabla.dc.shared.command.company.settings.UpdateFinancialYear;
import com.nabla.dc.shared.model.IPeriodEnd;
import com.nabla.wapp.client.model.HeterogeneousTreeModel;
import com.nabla.wapp.client.model.field.DateField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.TreeStringIdField;
import com.nabla.wapp.client.model.field.TreeStringParentIdField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class PeriodEndTreeModel extends HeterogeneousTreeModel<PeriodEndTreeRecord> {

	static public class Fields {
		public String name() { return IPeriodEnd.NAME; }
		public String endDate() { return IPeriodEnd.END_DATE; }
	}

	private static final Fields	fields = new Fields();
	private final Integer			companyId;

	public PeriodEndTreeModel(final Integer companyId) {
		super(PeriodEndTreeRecord.factory);

		this.companyId = companyId;
		setFields(
			new TreeStringIdField(),
			new TreeStringParentIdField(),
			new TextField(fields.name(), IPeriodEnd.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new DateField(fields.endDate(), FieldAttributes.OPTIONAL, FieldAttributes.READ_ONLY)
				);
	}
	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(final DSRequest request) {
		return new FetchPeriodEndTree(companyId, getParentId(request));
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final PeriodEndTreeRecord record) {
		return new UpdateFinancialYear(record.getId(), record.getName());
	}

}
