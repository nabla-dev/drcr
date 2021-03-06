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
import com.nabla.dc.shared.model.company.IPeriodEnd;
import com.nabla.wapp.client.model.IRecordFactory;
import com.nabla.wapp.client.model.data.HeterogeneousTreeGridRecord;
import com.smartgwt.client.data.Record;


public class PeriodEndTreeRecord extends HeterogeneousTreeGridRecord {

	public static final IRecordFactory<PeriodEndTreeRecord>	factory = new IRecordFactory<PeriodEndTreeRecord>() {
		@Override
		public PeriodEndTreeRecord get(final JavaScriptObject data) {
			return new PeriodEndTreeRecord(data);
		}
	};

	public PeriodEndTreeRecord(final Record impl) {
		super(impl);
	}

	public PeriodEndTreeRecord(final JavaScriptObject js) {
		super(js);
	}

	@Override
	public String getName() {
		return getAttributeAsString(IPeriodEnd.NAME);
	}

}
