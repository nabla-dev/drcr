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
package com.nabla.dc.server.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.nabla.dc.server.xml.settings.XmlSettings;
import com.nabla.dc.shared.command.ExportSettings;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.SimpleMatcher;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IntegerResult;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla64
 *
 */
public class ExportSettingsHandler extends AbstractHandler<ExportSettings, IntegerResult> {

	public ExportSettingsHandler() {
		super(true);
	}

	@Override
	public IntegerResult execute(@SuppressWarnings("unused") final ExportSettings cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		writeSettings(ctx.getReadConnection(), out);
		return new IntegerResult(exportSettings(ctx.getWriteConnection(), new ByteArrayInputStream(out.toByteArray())));
	}

	private void writeSettings(final Connection conn, final OutputStream out) throws SQLException, DispatchException {
		final XmlSettings settings = new XmlSettings(conn);
		final Serializer serializer = new Persister(new SimpleMatcher());
		try {
			serializer.write(settings, out);
		} catch (Exception e) {
			Util.throwInternalErrorException(e);
		}
	}

	private Integer exportSettings(final Connection conn, final InputStream in) throws SQLException, InternalErrorException {
		final Integer id = Database.addRecord(conn,
"INSERT INTO export (name,content_type,output_as_file,content) VALUES('settings.xml','text/xml',TRUE,?);", in);
		if (id == null)
			Util.throwInternalErrorException("fail to save settings to be exported");
		return id;
	}

}
