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
package com.nabla.dc.server.handler.fixed_asset;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.server.xml.assets.ImportContext;
import com.nabla.dc.server.xml.assets.SaveContext;
import com.nabla.dc.server.xml.assets.XmlCompanyAssets;
import com.nabla.dc.shared.command.fixed_asset.ImportCompanyAssets;
import com.nabla.dc.shared.model.fixed_asset.IImportAssets;
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


public class ImportCompanyAssetsHandler extends AbstractHandler<ImportCompanyAssets, StringResult> {

	private static final Log	log = LogFactory.getLog(ImportCompanyAssetsHandler.class);
	private final IDatabase	writeDb;

	@Inject
	public ImportCompanyAssetsHandler(@IReadWriteDatabase final IDatabase writeDb) {
		super(true);
		this.writeDb = writeDb;
	}

	@Override
	public StringResult execute(final ImportCompanyAssets cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ImportErrorManager errors = new ImportErrorManager(writeDb, cmd.getFileId());
		try {
			final JsonResponse json = new JsonResponse();
			json.put(IImportAssets.SUCCESS, add(cmd, errors, ctx));
			return json.toStringResult();
		} catch (DispatchException e) {
			throw e;
		} catch (Throwable e) {
			throw new InternalErrorException(Util.formatInternalErrorDescription(e));
		} finally {
			errors.close();
		}
	}

	private boolean add(final ImportCompanyAssets cmd, final IErrorList<Integer> errors, final IUserSessionContext ctx) throws DispatchException, SQLException {
		UserPreference.save(ctx, null, IImportAssets.PREFERENCE_GROUP, IImportAssets.OVERWRITE, cmd.getOverwrite());
		final XmlCompanyAssets assets = new Importer<ImportContext>(ctx.getReadConnection(), new ImportContext(ctx.getReadConnection(), cmd.getCompanyId(), errors)).read(XmlCompanyAssets.class, cmd.getFileId(), ctx.getSessionId());
		if (assets == null || !errors.isEmpty())
			return false;
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			SqlInsertOptions option = cmd.getOverwrite();
			if (option == SqlInsertOptions.REPLACE) {
				if (log.isDebugEnabled())
					log.debug("replacing old assets, so deleting all assets");
				assets.clear(guard.getConnection());
				option = SqlInsertOptions.INSERT;
			}
			return guard.setSuccess(assets.save(guard.getConnection(), new SaveContext(option)));
		} finally {
			guard.close();
		}
	}

}
