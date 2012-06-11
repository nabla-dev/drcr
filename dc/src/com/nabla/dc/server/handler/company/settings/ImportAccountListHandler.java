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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.company.settings.AccountCsvReader;
import com.nabla.dc.shared.command.company.settings.AddAccount;
import com.nabla.dc.shared.command.company.settings.ImportAccountList;
import com.nabla.dc.shared.model.company.settings.IImportAccount;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.database.SqlInsert;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class ImportAccountListHandler extends AbstractHandler<ImportAccountList, StringResult> {

	private static final Log		log = LogFactory.getLog(ImportAccountListHandler.class);
	private final IDatabase		writeDb;
	
	@Inject
	public ImportAccountListHandler(@IReadWriteDatabase final IDatabase writeDb) {
		super(true);
		this.writeDb = writeDb;
	}
	
	@Override
	public StringResult execute(final ImportAccountList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final ImportErrorManager errors = new ImportErrorManager(writeDb, cmd.getBatchId());
		try {
			final JsonResponse json = new JsonResponse();
			json.put(IImportAccount.SUCCESS, add(cmd, errors, ctx));
			return json.toStringResult();
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("failed to parse accounts from CSV data", e);
			Util.throwInternalErrorException(e);
		} finally {
			errors.close();
		}
		return null;
	}
	
	private boolean add(final ImportAccountList cmd, final ImportErrorManager errors, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final PreparedStatement stmtFile = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT content FROM import_data WHERE id=?;", cmd.getBatchId());
		try {
			final ResultSet rs = stmtFile.executeQuery();
			try {
				if (!rs.next()) {
					errors.add(ServerErrors.NO_DATA_TO_IMPORT);
					return false;
				}
				final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
				try {
					final SqlInsert<AddAccount> sql = new SqlInsert<AddAccount>(AddAccount.class, cmd.getOverwrite());
					final BatchInsertStatement<AddAccount> stmt = sql.prepareBatchStatement(ctx.getWriteConnection());
					try {
						final AccountCsvReader csv = new AccountCsvReader(rs.getCharacterStream("content"), errors);
						try {
							if (!csv.read(cmd, stmt))
								return false;
						} finally {
							csv.close();
						}
						stmt.execute();
					} finally {
						stmt.close();
					}
					UserPreference.save(ctx, IImportAccount.PREFERENCE_GROUP, IImportAccount.ROW_HEADER, cmd.isRowHeader());
					UserPreference.save(ctx, IImportAccount.PREFERENCE_GROUP, IImportAccount.OVERWRITE, cmd.getOverwrite());
					guard.setSuccess();
					return true;
				} finally {
					guard.close();
				}
			} finally {
				rs.close();
			}
		} finally {
			stmtFile.close();
		}
	}

}
