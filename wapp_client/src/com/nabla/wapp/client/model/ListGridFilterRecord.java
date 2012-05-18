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
package com.nabla.wapp.client.model;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.shared.model.IListGridFilter;
import com.smartgwt.client.data.AdvancedCriteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.JSON;

/**
 * The <code></code> object is used to
 *
 */
public class ListGridFilterRecord {

	protected final Record	impl;

	public ListGridFilterRecord(final Record impl) {
		Assert.argumentNotNull(impl);

		this.impl = impl;
	}

	public Record getImpl() {
		return impl;
	}

	public AdvancedCriteria getCriteria() {
		return new AdvancedCriteria(JSON.decode(impl.getAttributeAsString(IListGridFilter.VALUE)));
	}

}
