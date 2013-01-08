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
package com.nabla.wapp.report.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.print.ReportFormats;

public class Report {

	private final String			name;
	private final InputStream		impl;
	private final ReportFormats 	format;

	Report(final String name, final ByteArrayOutputStream impl, final ReportFormats format) {
		this.name = name;
		this.impl = new ByteArrayInputStream(impl.toByteArray());
		this.format = format;
	}

	public void close() {
		try {
			impl.close();
		} catch (IOException __) {}
	}

	public Integer save(final Connection conn, final Boolean outputAsFile, final String userSessionId) throws SQLException, InternalErrorException {
		final Integer id = Database.addRecord(conn,
"INSERT INTO export (name,content_type,output_as_file,content,userSessionId) VALUES(?,?,?,?,?);",
name, format.getMimeType(), outputAsFile, impl, userSessionId);
		if (id == null)
			Util.throwInternalErrorException("fail to save report to be exported '" + name + "'");
		return id;
	}
}
