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

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.csv.ICsvField;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla64
 *
 */
public class CsvReader<T> implements ICsvReader<T> {

	private static final Log						log = LogFactory.getLog(CsvReader.class);
	private static final Map<Class, ICsvSetter>	cache = new HashMap<Class, ICsvSetter>();

	static {
		cache.put(String.class, new StringSetter());
		cache.put(Integer.class, new IntegerSetter());
		cache.put(Boolean.class, new BooleanSetter());
	/*	cache.put(Date.class, new DateSetter());
		*/
	}

	private final Class<T>					recordClass;
	private final Map<String, ICsvColumn>	expectedColumns = new HashMap<String, ICsvColumn>();
	private final ICsvListReader			impl;
	private final ArrayList<ICsvColumn>	columns = new ArrayList<ICsvColumn>();
	private Method							validate;
	
	public CsvReader(final Reader reader, final Class<T> recordClass) {
		this.recordClass = recordClass;
		impl = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
		buildColumnList(recordClass);
		try {
			validate = recordClass.getMethod("validate");
		} catch (Throwable __) {
			validate = null;
		}
	}

	@Override
	public void readHeader() throws CsvException {
		String[] labels;
		try {
			labels = impl.getCSVHeader(true);
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error("error while reading csv header", e);
			throw new CsvException(1, CommonServerErrors.INTERNAL_ERROR);
		}
		if (labels == null)
			throw new CsvException(1, CommonServerErrors.NO_DATA);
		columns.clear();
		for (int i = 0; i < labels.length; ++i) {
			labels[i] = labels[i].toLowerCase();
			final ICsvColumn column = expectedColumns.get(labels[i].toLowerCase());
			if (column == null)
				throw new CsvException(1, labels[i], CommonServerErrors.UNSUPPORTED_FIELD);
			columns.add(column);
		}
	}
	
	public int getLineNumber() {
		return impl.getLineNumber();
	}
	
	@Override
	public boolean next(T instance) throws CsvException {
		Assert.argumentNotNull(instance);
		
		List<String> values;
		try {
			values = impl.read();
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error("error while reading next csv line", e);
			throw new CsvException(getLineNumber(), CommonServerErrors.INTERNAL_ERROR);
		}
		if (values == null)
			return false;
		if (columns.size() != values.size())
			throw new CsvException(getLineNumber(), CommonServerErrors.INVALID_FIELD_COUNT);
		int c = 0;
		try {
			for (; c < columns.size(); ++c)
				columns.get(c).setValue(instance, values.get(c));
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("error while reading next csv line", e);
			throw new CsvException(getLineNumber(), columns.get(c).getName(), CommonServerErrors.INVALID_VALUE);
		}
		if (validate != null) {
			try {
				validate.invoke(instance);
			} catch (InvocationTargetException e) {
				final Throwable ee = e.getCause();
				if (ee == null) {
					if (log.isErrorEnabled())
						log.error("error while reading next csv line", e);
					throw new CsvException(getLineNumber(), CommonServerErrors.INTERNAL_ERROR);
				} else if (ee.getClass().equals(ValidationException.class)) {
					final Map.Entry<String, String> error = ((ValidationException)ee).getError();
					throw new CsvException(getLineNumber(), error.getKey(), error.getValue());
				} else {
					if (log.isErrorEnabled())
						log.error("error while validating next csv line", ee);
					throw new CsvException(getLineNumber(), CommonServerErrors.INTERNAL_ERROR);
				}
			} catch (Throwable e) {
				if (log.isErrorEnabled())
					log.error("error while validating next csv line", e);
				throw new CsvException(getLineNumber(), CommonServerErrors.INTERNAL_ERROR);
			}
		}
		return true;
	}
	
	@Override
	public void close() {
		try { impl.close(); } catch (IOException e) {}
	}
	
	@SuppressWarnings("unchecked")
	private void buildColumnList(final Class recordClass) {
		if (recordClass != null) {
			for (Field field : recordClass.getDeclaredFields()) {
				final ICsvField definition = field.getAnnotation(ICsvField.class);
				if (definition != null) {
					final ICsvSetter writer = cache.get(field.getType());
					Assert.notNull(writer, "no CSV setter defined for type '" + field.getType().getSimpleName() + "'");
					if (!field.isAccessible())
						field.setAccessible(true);	// in order to lift restriction on 'private' fields
					final ICsvColumn column = new CsvColumn(field, writer);
					expectedColumns.put(column.getName().toLowerCase(), column);
					columns.add(column);
				}
			}
			buildColumnList(recordClass.getSuperclass());
		}
	}
	
}
