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
import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.wapp.client.model.BasicListGridRecord;
import com.nabla.wapp.client.model.IRecordFactory;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class CompanyRecord extends BasicListGridRecord implements ICompany {

	public static final IRecordFactory<CompanyRecord>	factory = new IRecordFactory<CompanyRecord>() {

		@Override
		public CompanyRecord get(JavaScriptObject data) {
			return new CompanyRecord(data);
		}

	};

	public CompanyRecord(Record impl) {
		super(impl);
	}

	public CompanyRecord(JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

	public Boolean getActive() {
		return getBoolean(ACTIVE);
	}

}
