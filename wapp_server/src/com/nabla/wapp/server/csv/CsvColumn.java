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
package com.nabla.wapp.server.csv;

import java.lang.reflect.Field;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.csv.ICsvField;

public class CsvColumn implements ICsvColumn {

	private final Field		field;
	private final String		name;
	private final ICsvSetter	writter;
	
	public CsvColumn(Field field, ICsvSetter writter) {
		Assert.argumentNotNull(field);
		Assert.argumentNotNull(writter);
		
		this.field = field;
		this.writter = writter;
		final ICsvField properties = field.getAnnotation(ICsvField.class);
		this.name = properties.name().isEmpty() ? field.getName() : properties.name();
	}

	@Override
	public void setValue(final Object record, String value) throws Exception {
		writter.setValue(record, field, value);
	}

	@Override
	public String getName() {
		return name;
	}

}
