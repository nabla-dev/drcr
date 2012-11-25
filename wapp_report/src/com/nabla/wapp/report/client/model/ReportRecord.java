/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.client.model;

import com.nabla.wapp.client.model.data.BasicListGridRecord;
import com.nabla.wapp.report.shared.IReport;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class ReportRecord extends BasicListGridRecord implements IReport {

	public ReportRecord(Record impl) {
		super(impl);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}

}