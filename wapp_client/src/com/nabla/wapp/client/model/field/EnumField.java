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

import java.util.LinkedHashMap;
import java.util.Map;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.fields.DataSourceEnumField;


public class EnumField extends DataSourceEnumField {

	public EnumField(String name, final FieldAttributes... attributes) {
		super(name);
		FieldAttributes.applyAll(this, attributes);
	}

	public <E extends Enum<E>> EnumField(String name, final Map<E, String> valueMap, final FieldAttributes... attributes) {
		super(name);
		setEnumMap(valueMap);
		FieldAttributes.applyAll(this, attributes);
	}

	public <E extends Enum<E>> EnumField(String name, final E[] values, final String[] valueAsString, final FieldAttributes... attributes) {
		super(name);
		setEnumMap(values, valueAsString);
		FieldAttributes.applyAll(this, attributes);
	}

	public <E extends Enum<E>> void setEnumMap(final Map<E, String> valueMap) {
		Assert.argumentNotNull(valueMap);

		final Map<String, String> values = new LinkedHashMap<String, String>();
		for (Map.Entry<E, String> value : valueMap.entrySet())
			values.put(value.getKey().toString(), value.getValue());
		setValueMap(values);
	}

	public <E extends Enum<E>> void setEnumMap(final E[] values, final String[] valueAsString) {
		Assert.argumentNotNull(values);
		Assert.argumentNotNull(valueAsString);
		Assert.state(values.length == valueAsString.length);

		final Map<String, String> valueMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < values.length; ++i)
			valueMap.put(values[i].toString(), valueAsString[i]);
		setValueMap(valueMap);
	}

	public <E extends Enum<E>> void setDefaultValue(E value) {
		setAttribute(IFieldReservedNames.DEFAULT_VALUE, value.toString());
	}

}
