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

import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import com.nabla.wapp.shared.model.IFieldReservedNames;


public class MonthField extends EnumField {

	public MonthField(final String name, final FieldAttributes... attributes) {
		super(name, attributes);
		final String[] monthNames = new DefaultDateTimeFormatInfo().monthsFull();
		final Map<String, String> values = new LinkedHashMap<String, String>();
		for (Integer i = 0; i < monthNames.length; ++i)
			values.put(i.toString(), monthNames[i]);
		setValueMap(values);
	}

	public MonthField(final String name, int defaultMonth, final FieldAttributes... attributes) {
		this(name, attributes);
		setAttribute(IFieldReservedNames.DEFAULT_VALUE, defaultMonth);
	}

}
