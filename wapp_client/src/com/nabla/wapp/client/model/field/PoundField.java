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
package com.nabla.wapp.client.model.field;

import com.nabla.wapp.client.model.PoundType;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.fields.DataSourceSimpleTypeField;

/**
 * @author nabla64
 *
 */
public class PoundField extends DataSourceSimpleTypeField {

	public PoundField(final String name, final FieldAttributes... attributes) {
		super(name, PoundType.instance);
		FieldAttributes.applyAll(this, attributes);
	}

	public void setDefaultValue(Integer value) {
		setAttribute(IFieldReservedNames.DEFAULT_VALUE, value);
	}
}
