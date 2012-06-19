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
package com.nabla.dc.client.model.company.settings;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.dc.shared.model.IPeriodEnd;
import com.nabla.wapp.client.model.BasicListGridRecord;
import com.nabla.wapp.client.model.IRecordFactory;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class PeriodEndRecord extends BasicListGridRecord implements IPeriodEnd {

	public static final IRecordFactory<PeriodEndRecord>	factory = new IRecordFactory<PeriodEndRecord>() {
		@Override
		public PeriodEndRecord get(final JavaScriptObject data) {
			return new PeriodEndRecord(data);
		}
	};

	public PeriodEndRecord(final Record impl) {
		super(impl);
	}

	public PeriodEndRecord(final JavaScriptObject js) {
		super(js);
	}

	public String getName() {
		return getAttributeAsString(NAME);
	}
	
	public Date getEndDate() {
		return getAttributeAsDate(END_DATE);
	}
	
	public Boolean getVisible() {
		return getBoolean(VISIBLE);
	}
	
	public Boolean isNominalLedgerOpened() {
		return getBoolean(NL_OPENED);
	}
	
	public Boolean isSalesLedgerOpened() {
		return getBoolean(SL_OPENED);
	}

	public Boolean isPurchaseLedgerOpened() {
		return getBoolean(PL_OPENED);
	}

	public Boolean isCashBookOpened() {
		return getBoolean(CB_OPENED);
	}
}
