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

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.company.IAccount;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.smartgwt.client.data.Record;


public class AccountRecord extends BasicListGridRecord implements IAccount {

	public static final IRecordFactory<AccountRecord>	factory = new IRecordFactory<AccountRecord>() {
		@Override
		public AccountRecord get(final JavaScriptObject data) {
			return new AccountRecord(data);
		}
	};

	public AccountRecord(final Record impl) {
		super(impl);
	}

	public AccountRecord(final JavaScriptObject js) {
		super(js);
	}

	public String getCode() {
		return getAttributeAsString(CODE);
	}

	public String getCostCentre() {
		return getAttributeAsString(COST_CENTRE);
	}

	public String getDepartment() {
		return getAttributeAsString(DEPARTMENT);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

	public Boolean getActive() {
		return getBoolean(ACTIVE);
	}

	public Boolean isBalanceSheet() {
		return getBoolean(BALANCE_SHEET);
	}
}
