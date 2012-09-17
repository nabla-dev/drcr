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

import com.google.gwt.event.shared.HandlerRegistration;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.IApplication;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.server.IDispatchAsync;
import com.nabla.wapp.shared.model.ValidationException;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public abstract class Model extends DataSource {

	private static final Logger				logger = LoggerFactory.getLog(Model.class);
	protected IDispatchAsync					dispatcher = null;
	private final Map<String, DataSourceField>	fields = new HashMap<String, DataSourceField>();
	protected Record[]							clientOnlyData;

	protected Model() {
		this.setDataProtocol(DSProtocol.CLIENTCUSTOM);
		this.setDataFormat(DSDataFormat.CUSTOM);	// needed ?
		this.setClientOnly(false);	// needed ?
	}

	public void setDispatcher(final IDispatchAsync dispatcher) {
		this.dispatcher = dispatcher;
	}

	public IDispatchAsync getDispatcher() {
		return dispatcher;
	}

	@Override
	public void setFields(final DataSourceField... fields) {
		for (final DataSourceField field : fields)
			this.fields.put(field.getName(), field);
		super.setFields(fields);
	}

	@Override
	public DataSourceField getField(final String name) {
		return fields.get(name);
	}

	protected void onResponse(final DSRequest request, final DSResponse response) {
		processResponse(request.getRequestId(), response);
	}

	void onFailure(final DSRequest request, final Throwable caught) {
		final IApplication app = Application.getInstance();
		if (caught != null && caught instanceof ValidationException) {
			// convert error code to message
			final ValidationException x = (ValidationException) caught;
			onResponse(request, new Response(request, x.getErrorMessages(app.getServerErrorResource())));
		} else {
			// prevent SmartGWT from displaying its own error message box
			final HandlerRegistration handler = this.addHandleErrorHandler(new HandleErrorHandler() {
				@Override
				public void onHandleError(ErrorEvent event) {
					event.cancel();
				}
			});
			try {
				onResponse(request, new Response(request, Response.STATUS_FAILURE));
				app.getMessageBox().error(caught);
			} finally {
				handler.removeHandler();
			}
		}
	}

	void onFailureUploadFile(final DSRequest request, final String fieldName, final String errorCode) {
		onResponse(request, new Response(request, fieldName, Application.getInstance().getLocalizedError(errorCode)));
	}

	protected Record[] getEditedRecords(final DSRequest request) {
		return combineRecords(	Record.convertToRecordArray(request.getAttributeAsJavaScriptObject("oldValues")),
								Record.convertToRecordArray(request.getData())
								);
	}

	protected Record[] combineRecords(final Record[] oldValues, final Record[] newValues) {
		Assert.argumentNotNull(oldValues);
		Assert.argumentNotNull(newValues);

		if (newValues.length == 0)
			return oldValues;
		if (oldValues.length == 0)
			return newValues;
		if (newValues.length != oldValues.length)
			throw new IllegalArgumentException("Old and new value arrays don't have the same dimension");
		logger.fine("combining old and new values to get records");
		final ListGridRecord[] result = new ListGridRecord[newValues.length];
		for (int i = 0; i < newValues.length; ++i) {
			result[i] = new ListGridRecord();
			JSOHelper.apply(oldValues[i].getJsObj(), result[i].getJsObj());
			JSOHelper.apply(newValues[i].getJsObj(), result[i].getJsObj());
		}
		return result;
	}

}
