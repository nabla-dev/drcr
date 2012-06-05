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

	private static final long			serialVersionUID = 1L;
	public static final String		ERROR_CODE = "VALIDATION_ERROR";
	// field, errorNo
	private final Map<String, String>	errors = new HashMap<String, String>();

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

	public boolean isEmpty() {
		return errors.isEmpty();
	}
	
	public void add(final String field, final String error) {
		errors.put(field, error);
	}

	public <E extends Enum<E>> void add(final String field, final E error) {
		add(field, error.toString());
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public String getError(final String field) {
		return errors.get(field);
	}

	public Map.Entry<String, String> getError() {
		if (errors.isEmpty())
			return null;
		return errors.entrySet().iterator().next();
	}
	
	public Map<String, String> getErrorMessages(final ConstantsWithLookup resource) {
		if (resource == null)
			throw new IllegalArgumentException("resource");
		final Map<String, String> ret = new HashMap<String, String>();
		for (final Map.Entry<String, String> e : errors.entrySet())
				ret.put(e.getKey(), resource.getString(e.getValue()));
		return ret;
	}

}
