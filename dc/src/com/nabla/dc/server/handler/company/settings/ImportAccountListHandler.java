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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.company.settings.ImportAccountList;
import com.nabla.dc.shared.model.IAccount;
import com.nabla.dc.shared.model.company.settings.IImportAccount;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class ImportAccountListHandler extends AbstractHandler<ImportAccountList, StringResult> {

	private static final Log		log = LogFactory.getLog(ImportAccountListHandler.class);
	private static final String[]	header = new String[] {
		"code", "cc", "dep", "name", "bs"
	};
	private final Set<String>		allowedLabels;
	private final IDatabase			writeDb;

	static class Record {

		String		code;
		String		cost_centre;
		String		department;
		String		name;
		String		uname;
		Boolean		balance_sheet;

		public void validate() throws ValidationException, SQLException {
			IAccount.CODE_CONSTRAINT.validate(IAccount.CODE, code);
			uname = name.toUpperCase();
		}

	}

	@Inject
	public ImportAccountListHandler(@IReadWriteDatabase final IDatabase writeDb) {
		this.writeDb = writeDb;
		allowedLabels = new HashSet<String>(Arrays.asList(header));
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
	
	private boolean add(final ImportAccountList cmd, final ImportErrorManager errors, final IUserSessionContext ctx) throws DispatchException, SQLException, IOException {
		// load CSV file
		final PreparedStatement stmtFile = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT content FROM import_data WHERE id=?;", cmd.getBatchId());
		try {
			final ResultSet rs = stmtFile.executeQuery();
			if (!rs.next()) {
				errors.add(0, ServerErrors.NO_DATA_TO_IMPORT);
				return false;
			}
			final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
			try {
				final PreparedStatement stmt = ctx.getWriteConnection().prepareStatement(
"INSERT INTO account (company_id,active,code,name,uname,cost_centre,department,balance_sheet) VALUES(?,?,?,?,?,?,?,?);");
				try {
					stmt.setInt(1, cmd.getCompanyId());
					stmt.setBoolean(2, true);
					final ICsvBeanReader csv = new CsvBeanReader(rs.getCharacterStream("content"), CsvPreference.STANDARD_PREFERENCE);
					try {
						// extract header if any
				    	String[] labels;
						if (cmd.isRowHeader()) {
							labels = csv.getCSVHeader(true);
							if (labels == null) {
								errors.add(0, ServerErrors.NO_DATA_TO_IMPORT);
								return false;
							}
							for (int i = 0; i < labels.length; ++i) {
								labels[i] = labels[i].toLowerCase();
								if (!allowedLabels.contains(labels[i])) {
									errors.add(1, labels[i], ServerErrors.UNSUPPORTED_IMPORT_COLUMN);
									return false;
								}
							}
						} else {
							labels = header;
						}
						CellProcessor[] formatters = new CellProcessor[labels.length];
						
						
					} finally {
						csv.close();
					}
					if (!Database.isBatchCompleted(stmt.executeBatch()))
						Util.throwInternalErrorException("failed to save acounts to database");
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
				UserPreference.save(ctx, IImportAccount.PREFERENCE_GROUP, IImportAccount.ROW_HEADER, cmd.isRowHeader());
				UserPreference.save(ctx, IImportAccount.PREFERENCE_GROUP, IImportAccount.OVERWRITE, cmd.getOverwrite());
				guard.setSuccess();
				return true;
			} finally {
				guard.close();
			}
		} finally {
			try { stmtFile.close(); } catch (final SQLException e) {}
		}
		/*	final List<Transaction> transactions = convertCsvToList(request, ctx.getWriteConnection());
		if (transactions == null || transactions.isEmpty())*/
	/*	int fromTransNo = Integer.MAX_VALUE;
		int toTransNo = 0;
		for (Transaction e : transactions) {
			fromTransNo = Math.min(fromTransNo, e.id);
			toTransNo = Math.max(toTransNo, e.id);
		}
				for (Transaction transaction : transactions) {
					stmt.setDate(2, transaction.date);
					stmt.setString(3, transaction.reference);
					stmt.setString(4, transaction.text);
					stmt.setInt(5, transaction.amount);
					stmt.setBoolean(6, transaction.recharge);
					stmt.setInt(7, transaction.id);
					stmt.addBatch();
				}
		}*/
	}
/*
							} else if (ee.getClass().equals(ValidationException.class)) {
								final Map.Entry<String, String> error = ((ValidationException)ee).getError();
								if (!errors.add(lineNo, error.getKey(), error.getValue()))
									return null;
							} else if (ee.getClass().equals(ValueRequiredException.class)) {
								if (log.isDebugEnabled())
									log.debug("error", ee);
								if (!errors.add(lineNo, extractFieldName(ee.getMessage()), CommonServerErrors.REQUIRED_VALUE.toString()))
									return null;
							} else {
								if (log.isErrorEnabled())
									log.error("error while deserializing xml request", ee);
								if (!errors.add(lineNo, "", CommonServerErrors.INTERNAL_ERROR.toString()))
									return null;
							}
						} catch (final Exception e) {
							if (log.isDebugEnabled())
								log.debug("error", e);
							if (!errors.add(lineNo, e.getLocalizedMessage()))
								return null;
						}
					}
				}
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("failed to parse CSV data", e);
			Util.throwInternalErrorException(e);
		
	}

	private static String extractFieldName(final String simpleErrorMessage) {
		final String[] matches = simpleErrorMessage.split("'");
		return (matches.length < 3) ? "?" : matches[1];
	}
*/
}
