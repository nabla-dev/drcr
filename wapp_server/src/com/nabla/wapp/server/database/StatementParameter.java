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
package com.nabla.wapp.server.database;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.nabla.wapp.shared.database.IRecordField;


public class StatementParameter implements IStatementParameter {

	private final Field			field;
	private final String			name;
	private final boolean			includeNull;
	private final boolean			unique;
	private final IStatementSetter	writer;

	public StatementParameter(final Field field, final IStatementSetter writer) {
		this.field = field;
		final IRecordField properties = field.getAnnotation(IRecordField.class);
		this.name = properties.name().isEmpty() ? field.getName() : properties.name();
		this.includeNull = properties.required();
		this.unique = properties.unique();
		this.writer = writer;
	}

	@Override
	public void write(final PreparedStatement stmt, int parameterIndex, Object record) throws SQLException {
		writer.setValue(stmt, parameterIndex, getValue(record));
	}

	@Override
	public boolean include(Object record) {
		return getValue(record) != null || includeNull;
	}

	private Object getValue(Object record) {
		try {
			return field.get(record);
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isUnique() {
		return unique;
	}
}
