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

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.model.IRecordFactory;
import com.smartgwt.client.data.Record;


public class AddCompanyRecord extends CompanyRecord {

	public static final IRecordFactory<AddCompanyRecord>	factory = new IRecordFactory<AddCompanyRecord>() {

		@Override
		public AddCompanyRecord get(JavaScriptObject data) {
			return new AddCompanyRecord(data);
		}

	};

	public AddCompanyRecord(Record impl) {
		super(impl);
	}

	public AddCompanyRecord(JavaScriptObject js) {
		super(js);
	}

	public String getFinancialYear() {
		return getAttributeAsString(AddCompanyModel.Fields.financialYear());
	}

	public Date getStartDate() {
		return getAttributeAsDate(AddCompanyModel.Fields.startDate());
	}

}
