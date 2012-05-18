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
package com.nabla.wapp.client.model;

import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.model.IListGridFilter;

/**
 * The <code></code> object is used to
 *
 */
public class ListGridFilterListModel extends CModel {

	static public class Fields {
		public String name() { return IListGridFilter.NAME; }
	}

	private static final Fields	fields = new Fields();
	private final String filterName;
	
	public ListGridFilterListModel(final String filterName) {
		super();
		this.filterName = filterName;
		setFields(
			new IdField(),
			new TextField(IListGridFilter.NAME, IListGridFilter.NAME_CONSTRAINT, FieldAttributes.REQUIRED),
			new TextField(IListGridFilter.VALUE, IListGridFilter.VALUE_CONSTRAINT, FieldAttributes.OPTIONAL, FieldAttributes.HIDDEN)
				);
		setCacheAllData(true);
	}
/*
	@Override
	public AbstractRemove getRemoveCommand() {
		return new RemoveListGridFilter(filterName);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchListGridFilterList(filterName);
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final Record record) {
		return new UpdateListGridFilter(filterName, record.getId());
	}
*/
}
