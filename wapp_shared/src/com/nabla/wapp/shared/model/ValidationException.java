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
package com.nabla.wapp.shared.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.nabla.wapp.shared.dispatch.ActionException;


/**
 * @author nabla
 *
 */
public class ValidationException extends ActionException {

	public static final String		ERROR_CODE = "VALIDATION_ERROR";

	private static final long		serialVersionUID = 1L;

	// row, field, errorNo
	private Map<Integer, Map<String, String>>	errors = new HashMap<Integer, Map<String, String>>();

	public ValidationException() {
		super(ERROR_CODE);
	}

	public ValidationException(final String field, final String error) {
		super(ERROR_CODE);
		add(field, error);
	}

	public <E extends Enum<E>> ValidationException(final String field, final E error) {
		super(ERROR_CODE);
		add(field, error);
	}

	public void add(int row, final String field, final String error) {
		Map<String, String> rowErrors = errors.get(row);
		if (rowErrors == null) {
			rowErrors = new HashMap<String, String>();
			rowErrors.put(field, error);
			errors.put(row, rowErrors);
		} else
			rowErrors.put(field, error);
	}

	public <E extends Enum<E>> void add(int row, final String field, final E error) {
		Map<String, String> rowErrors = errors.get(row);
		if (rowErrors == null) {
			rowErrors = new HashMap<String, String>();
			rowErrors.put(field, error.toString());
			errors.put(row, rowErrors);
		} else
			rowErrors.put(field, error.toString());
	}

	public void add(final String field, final String error) {
		add(0, field, error);
	}

	public <E extends Enum<E>> void add(final String field, final E error) {
		add(0, field, error);
	}

	public Map<Integer, Map<String, String>> getErrors() {
		return errors;
	}

	public Map<String, String> getErrors(int row) {
		return errors.get(row);
	}

	public Map.Entry<String, String> getError(int row) {
		final Map<String, String> rowErrors = getErrors(row);
		return (rowErrors == null) ? null : rowErrors.entrySet().iterator().next();
	}

	public Map.Entry<String, String> getError() {
		return getError(0);
	}

	public String getError(int row, final String field) {
		final Map<String, String> rowErrors = getErrors(row);
		return (rowErrors == null) ? null : rowErrors.get(field);
	}

	public String getError(final String field) {
		return getError(0, field);
	}

	public Map<Integer, Map<String, String>> getErrorMessages(final ConstantsWithLookup resource) {
		if (resource == null)
			throw new IllegalArgumentException("resource");
		final Map<Integer, Map<String, String>> ret = new HashMap<Integer, Map<String, String>>();
		for (final Map.Entry<Integer, Map<String, String>> e : errors.entrySet()) {
			final Map<String, String> rowErrors = new HashMap<String, String>();
			for (final Map.Entry<String, String> ee : e.getValue().entrySet())
				rowErrors.put(ee.getKey(), resource.getString(ee.getValue()));
			ret.put(e.getKey(), rowErrors);
		}
		return ret;
	}

}
