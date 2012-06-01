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
package com.nabla.dc.server.handler.company.settings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.dc.shared.command.company.settings.ChangeCompanyLogo;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class ChangeCompanyLogoHandler extends AbstractHandler<ChangeCompanyLogo, StringResult> {

	private static final Log	log = LogFactory.getLog(ChangeCompanyLogoHandler.class);

	private static final UpdateStatement<ChangeCompanyLogo>	sql = new UpdateStatement<ChangeCompanyLogo>(ChangeCompanyLogo.class);

	public ChangeCompanyLogoHandler() {
		super(true);
	}

	@Override
	public StringResult execute(ChangeCompanyLogo record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getWriteConnection(), Statement.RETURN_GENERATED_KEYS,
"INSERT INTO image (name,content_type,length,content)" + 
" SELECT file_name AS 'name',content_type,length,content FROM import_data WHERE id=?;", record.getFileId());
		try {
			if (stmt.executeUpdate() != 1)
				Util.throwInternalErrorException("failed to copy image from imported data to logo table");
			final ResultSet rsKey = stmt.getGeneratedKeys();
			try {
				rsKey.next();
				record.setLogoId(rsKey.getInt(1));
			} finally {
				Database.close(rsKey);
			}
		} finally {
			Database.close(stmt);
		}
		sql.execute(ctx.getWriteConnection(), record);
		return null;
	}

}
