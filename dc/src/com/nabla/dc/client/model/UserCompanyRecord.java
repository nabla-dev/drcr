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

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.IUserCompany;
import com.nabla.wapp.client.model.BasicRecord;
import com.nabla.wapp.client.model.IRecordFactory;
import com.smartgwt.client.data.Record;

/**
 * @author FNorais
 *
 */
public class UserCompanyRecord extends BasicRecord implements IUserCompany {

	public static final IRecordFactory<UserCompanyRecord>	factory = new IRecordFactory<UserCompanyRecord>() {
		@Override
		public UserCompanyRecord get(final JavaScriptObject data) {
			return new UserCompanyRecord(data);
		}
	};

	public UserCompanyRecord(final Record impl) {
		super(impl);
	}

	public UserCompanyRecord(final JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}
}
