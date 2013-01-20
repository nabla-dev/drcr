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

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.server.xml.settings.ImportContext;
import com.nabla.dc.server.xml.settings.SaveContext;
import com.nabla.dc.server.xml.settings.XmlSettings;
import com.nabla.dc.shared.command.ImportSettings;
import com.nabla.dc.shared.model.IImportSettings;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;


public class ImportSettingsHandler extends AbstractHandler<ImportSettings, StringResult> {

	private static final Log	log = LogFactory.getLog(ImportSettingsHandler.class);
	private final IDatabase	writeDb;

	@Inject
	public ImportSettingsHandler(@IReadWriteDatabase final IDatabase writeDb) {
		super(true);
		this.writeDb = writeDb;
	}

	@Override
	public StringResult execute(final ImportSettings cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ImportErrorManager errors = new ImportErrorManager(writeDb, cmd.getBatchId());
		try {
			final JsonResponse json = new JsonResponse();
			json.put(IImportSettings.SUCCESS, add(cmd, errors, ctx));
			return json.toStringResult();
		} catch (DispatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		} finally {
			errors.close();
		}
	}

	private boolean add(final ImportSettings cmd, final IErrorList<Integer> errors, final IUserSessionContext ctx) throws DispatchException, SQLException {
		UserPreference.save(ctx, null, IImportSettings.PREFERENCE_GROUP, IImportSettings.OVERWRITE, cmd.getOverwrite());
		final XmlSettings settings = new Importer<ImportContext>(ctx.getReadConnection(), new ImportContext(errors)).read(XmlSettings.class, cmd.getBatchId(), ctx.getSessionId());
		if (settings == null || !errors.isEmpty())
			return false;
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			SqlInsertOptions option = cmd.getOverwrite();
			if (option == SqlInsertOptions.REPLACE) {
				if (log.isDebugEnabled())
					log.debug("replacing old settings, so deleting all settings");
				settings.clear(conn);
				option = SqlInsertOptions.INSERT;
			}
			return guard.setSuccess(settings.save(conn, new SaveContext(conn, option, errors)));
		} finally {
			guard.close();
		}
	}

}
