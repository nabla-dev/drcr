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
package com.nabla.dc.client.model;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.nabla.dc.shared.command.FetchImportErrorList;
import com.nabla.dc.shared.model.IImportError;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.IntegerField;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.Record;


public class ImportErrorListModel extends CModel<Record> {

	static public class Fields implements IImportError {
		public String lineNo() { return LINE; }
		public String field() { return FIELD; }
		public String error() { return ERROR; }
	}

	private static final Fields	fields = new Fields();
	private final Integer			batchId;

	public ImportErrorListModel(final Integer batchId) {
		super();

		this.batchId = batchId;
		setFields(
			new IntegerField(fields.lineNo()),
			new TextField(fields.field()),
			new TextField(fields.error())
				);
	}

	public Fields fields() {
		return fields;
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchImportErrorList(batchId);
	}

	@Override
	protected Record[] recordsFromJson(final String jsonRecords) {
		// convert error code to human readable error message
		final Record[] records = super.recordsFromJson(jsonRecords);
		if (records != null) {
			final ConstantsWithLookup errorMessages = Application.getInstance().getServerErrorResource();
			for (Record record : records) {
				try {
					final String message = errorMessages.getString(record.getAttribute(fields.error()));
					record.setAttribute(fields.error(), message);
				} catch (final Throwable _) {}
			}
		}
		return records;
	}
}
