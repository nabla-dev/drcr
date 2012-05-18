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
package com.nabla.dc.client.model;

import com.nabla.dc.shared.command.FetchUserCompanyList;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.ImageField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class UserCompanyListModel extends CModel<Record> {

	static public class Fields {
		public String name() { return "name"; }
		public String image() { return "image"; }
	}

	private static final Fields	fields = new Fields();

	public UserCompanyListModel() {
		super();

		setFields(
			new IdField(),
			new TextField("name"),
			new ImageField("image")
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchUserCompanyList();
	}

}
