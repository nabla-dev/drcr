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
package com.nabla.dc.client.model.settings;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.ICompany;
import com.nabla.wapp.client.model.BasicRecord;
import com.nabla.wapp.client.model.IRecordFactory;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class CompanyLogoRecord extends BasicRecord implements ICompany {

	public static final IRecordFactory<CompanyLogoRecord>	factory = new IRecordFactory<CompanyLogoRecord>() {

		@Override
		public CompanyLogoRecord get(JavaScriptObject data) {
			return new CompanyLogoRecord(data);
		}

	};

	public CompanyLogoRecord(Record impl) {
		super(impl);
	}

	public CompanyLogoRecord(JavaScriptObject js) {
		super(js);
	}

	public Integer getFileId() {
		return getAttributeAsInt(LOGO_FILE);
	}

}
