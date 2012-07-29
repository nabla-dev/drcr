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
package com.nabla.wapp.client.model.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.general.JSHelper;
import com.nabla.wapp.client.model.field.IdField;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
public class BasicRecord extends Record {

	public BasicRecord(final Record impl) {
		super(impl.getJsObj());
	}

	public BasicRecord(final JavaScriptObject js) {
		super(js);
	}

	public Integer getId() {
		return getAttributeAsInt(IdField.NAME);
	}

	public boolean isNew() {
		return !JSHelper.isAttribute(getJsObj(), IdField.NAME);
	}

	protected Boolean getBoolean(final String attribute) {
		return JSHelper.getAttributeAsBoolean(this.getJsObj(), attribute);
	}

}
