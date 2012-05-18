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
package com.nabla.wapp.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;

/**
 * @author nabla
 *
 */
class Response extends DSResponse {

	private static final Logger		logger = LoggerFactory.getLog(Response.class);
	public static final String		CLIENT_CONTEXT = "clientContext";

	public Response(final DSRequest request) {
		Assert.argumentNotNull(request);

		this.setAttribute(CLIENT_CONTEXT, request.getAttributeAsObject(CLIENT_CONTEXT));
	}

	public Response(final DSRequest request, final int status) {
		this(request);
		this.setStatus(status);
	}

	public Response(final DSRequest request, final Map<Integer, Map<String, String>> fieldErrors) {
		this(request, STATUS_VALIDATION_ERROR);
		Assert.argumentNotNull(fieldErrors);

		final Map<String, JavaScriptObject> dsErrors = new HashMap<String, JavaScriptObject>();
		for (final Map.Entry<Integer, Map<String, String>> rowError : fieldErrors.entrySet()) {
			for (final Map.Entry<String, String> error : rowError.getValue().entrySet()) {
				logger.fine("field error: '" + error.getKey() + "' = '" + error.getValue() + "' at row " + rowError.getKey());
				final ValidationErrorRecord msg = new ValidationErrorRecord(rowError.getKey(), error.getValue());
				dsErrors.put(error.getKey(), msg.getJsObj());
			}
		}
		this.setErrors(dsErrors);
	}

	public Response(final DSRequest request, final String fieldName, final String error) {
		this(request, STATUS_VALIDATION_ERROR);

		logger.fine("field error: '" + fieldName + "' = '" + error);
		final Map<String, JavaScriptObject> dsErrors = new HashMap<String, JavaScriptObject>();
		final ValidationErrorRecord msg = new ValidationErrorRecord(0, error);
		dsErrors.put(fieldName, msg.getJsObj());
		this.setErrors(dsErrors);
	}

	public Response(final DSRequest request, final Record[] records) {
		this(request, STATUS_SUCCESS);
		this.setData(records);
	}

}
