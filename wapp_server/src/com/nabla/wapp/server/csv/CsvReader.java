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
import com.nabla.wapp.shared.model.FullErrorListException;
import com.nabla.wapp.shared.model.IErrorList;

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

	protected final ICsvErrorList			errors;
//	private final Class<T>					recordClass;
	private final Map<String, ICsvColumn>	expectedColumns = new HashMap<String, ICsvColumn>();
	private final ICsvListReader			impl;
	private final ArrayList<ICsvColumn>	columns = new ArrayList<ICsvColumn>();
	private Method							validate;

	public CsvReader(final Reader reader, final Class<T> recordClass, final ICsvErrorList errors) {
		Assert.argumentNotNull(recordClass);
		Assert.argumentNotNull(errors);

		this.errors = errors;
//		this.recordClass = recordClass;
		impl = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
		buildColumnList(recordClass);
		try {
			validate = recordClass.getMethod("validate", IErrorList.class);
		} catch (Throwable __) {
			validate = null;
		}
	}

	public ICsvErrorList getErrors() {
		return errors;
	}

	@Override
	public boolean readHeader() throws FullErrorListException {
		try {
			errors.setLine(1);
			String[] labels;
			try {
				labels = impl.getCSVHeader(true);
			} catch (IOException e) {
				if (log.isErrorEnabled())
					log.error("error while reading csv header", e);
				errors.add(CommonServerErrors.INTERNAL_ERROR);
				return false;
			}
			if (labels == null) {
				errors.add(CommonServerErrors.NO_DATA);
				return false;
			}
			columns.clear();
			for (int i = 0; i < labels.length; ++i) {
				labels[i] = labels[i].toLowerCase();
				final ICsvColumn column = expectedColumns.get(labels[i]);
				if (column == null) {
					errors.add(labels[i], CommonServerErrors.UNSUPPORTED_FIELD);
				} else
					columns.add(column);
			}
			return errors.isEmpty();
		} catch (FullErrorListException e) {
			throw e;
		} catch (Throwable e) {
			return false;
		}
	}

	public int getLineNumber() {
		return impl.getLineNumber();
	}

	@Override
	public Status next(T instance) throws FullErrorListException {
		Assert.argumentNotNull(instance);

		try {
			List<String> values;
			try {
				values = impl.read();
			} catch (IOException e) {
				if (log.isErrorEnabled())
					log.error("error while reading next csv line", e);
				errors.setLine(getLineNumber());
				errors.add(CommonServerErrors.INTERNAL_ERROR);
				return Status.ERROR;
			}
			if (values == null)
				return Status.EOF;
			errors.setLine(getLineNumber());
			if (columns.size() != values.size()) {
				errors.add(CommonServerErrors.INVALID_FIELD_COUNT);
				return Status.ERROR;
			}
			for (int c = 0; c < columns.size(); ++c) {
				try {
					columns.get(c).setValue(instance, values.get(c));
				} catch (Throwable e) {
					if (log.isErrorEnabled())
						log.error("error while reading next csv line", e);
					errors.add(columns.get(c).getName(), CommonServerErrors.INVALID_VALUE);
				}
			}
			try {
				if (validate != null)
					validate.invoke(instance, errors);
			} catch (final InvocationTargetException e) {
				final Throwable ee = e.getCause();
				if (log.isErrorEnabled())
					log.error("error while validating next csv line", ee);
				if (ee != null && ee.getClass().equals(FullErrorListException.class))
					throw new FullErrorListException();
				else {
					errors.add(CommonServerErrors.INTERNAL_ERROR);
					return Status.ERROR;
				}
			}
			return errors.isEmpty() ? Status.SUCCESS : Status.ERROR;
		} catch (FullErrorListException e) {
			throw e;
		} catch (Throwable e) {
			return Status.ERROR;
		}
	}

	@Override
	public void close() {
		try { impl.close(); } catch (IOException e) {}
	}

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
