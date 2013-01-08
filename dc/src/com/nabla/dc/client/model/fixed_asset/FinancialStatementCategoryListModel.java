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


import com.nabla.dc.shared.command.fixed_asset.AddFinancialStatementCategory;
import com.nabla.dc.shared.command.fixed_asset.FetchFinancialStatementCategoryList;
import com.nabla.dc.shared.command.fixed_asset.RemoveFinancialStatementCategory;
import com.nabla.dc.shared.command.fixed_asset.UpdateFinancialStatementCategory;
import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.BooleanField;
import com.nabla.wapp.client.model.field.DeletedRecordField;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;


public class FinancialStatementCategoryListModel extends CModel<FinancialStatementCategoryRecord> {

	static public class Fields {
		public String name() { return IFinancialStatementCategory.NAME; }
		public String active() { return IFinancialStatementCategory.ACTIVE; }
	}

	private static final Fields	fields = new Fields();

	public FinancialStatementCategoryListModel() {
		super(FinancialStatementCategoryRecord.factory);

		setFields(
			new DeletedRecordField(),
			new IdField(),
			new TextField(fields.name(), IFinancialStatementCategory.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new BooleanField(fields.active())
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveFinancialStatementCategory();
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchFinancialStatementCategoryList();
	}

	@Override
	public IRecordAction<StringResult> getAddCommand(final FinancialStatementCategoryRecord record) {
		return new AddFinancialStatementCategory(record.getName(), record.getActive());
	}

	@Override
	public IRecordAction<StringResult> getUpdateCommand(final FinancialStatementCategoryRecord record) {
		return new UpdateFinancialStatementCategory(record.getId(), record.getName(), record.getActive());
	}

}
