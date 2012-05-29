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

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.dc.shared.command.company.settings.ImportAccountList;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;

/**
 * @author nabla
 *
 */
public class ImportAccountListHandler extends AbstractAddHandler<ImportAccountList> {

	static final Log			log = LogFactory.getLog(ImportAccountListHandler.class);
/*
	static final String[]		header = new String[] {
		"nc", "name", "date", "value", "reference", "text", "id"
	};

	@Root(name="data")
	static class Record {
		@Element
		Integer		accountId;
		@Element
		String		name;
		@Element
		Integer		file;
		@Element
		Boolean		row_header;

		@Element(required=false)
		Integer		id;

		@Validate
		public void validate() throws ValidationException {
			IImportBatch.NAME_CONSTRAINT.validate(IImportBatch.NAME, name);
		}

	}

	@Root(name="record")
	static class Transaction {

		@Element
		String		nc;
		@Element
		String		name;
		@Element
		Date		date;
		@Element(required=false)
		String		reference;
		@Element(required=false)
		String		text;
		@Element
		Float		value;
		@Element
		Integer		id;

		Integer		amount;
		Boolean		recharge;

		@Validate
		public void validate() throws ValidationException, SQLException {
			if (reference != null)
				reference = reference.trim();
			if (text != null)
				text = text.trim();
			ITransaction.REFERENCE_CONSTRAINT.validate(ITransaction.REFERENCE, reference);
			ITransaction.TEXT_CONSTRAINT.validate(ITransaction.TEXT, text);
			amount = Math.round(value * 100);
			recharge = (text != null && text.toLowerCase().startsWith("s/l"));
		}

	}
*/
	@Override
	protected int add(final ImportAccountList cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
	/*	final List<Transaction> transactions = convertCsvToList(request, ctx.getWriteConnection());
		if (transactions == null || transactions.isEmpty())*/
			return -1;
	/*	int fromTransNo = Integer.MAX_VALUE;
		int toTransNo = 0;
		for (Transaction e : transactions) {
			fromTransNo = Math.min(fromTransNo, e.id);
			toTransNo = Math.max(toTransNo, e.id);
		}
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			final Integer batchId = Database.addRecord(ctx.getWriteConnection(),
"INSERT INTO import_batch (name,account_id,from_trans_id,to_trans_id) VALUES(?,?,?,?);", request.name, request.accountId, fromTransNo,toTransNo);
			final PreparedStatement stmt = ctx.getWriteConnection().prepareStatement(
"INSERT INTO transaction (import_batch_id,date,reference,text,amount,recharge,trans_id) VALUES(?,?,?,?,?,?,?);");
			try {
				stmt.setInt(1, batchId);
				for (Transaction transaction : transactions) {
					stmt.setDate(2, transaction.date);
					stmt.setString(3, transaction.reference);
					stmt.setString(4, transaction.text);
					stmt.setInt(5, transaction.amount);
					stmt.setBoolean(6, transaction.recharge);
					stmt.setInt(7, transaction.id);
					stmt.addBatch();
				}
				if (!Database.isBatchCompleted(stmt.executeBatch()))
					Util.throwInternalErrorException("failed to save transactions to database");
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
			UserPreference.save(ctx, ITransaction.IMPORT_PREFERENCE_GROUP, "row_header", request.row_header);
			guard.setSuccess();
			return serialize(batchId);
		} finally {
			guard.close();
		}*/
	}
/*
	private List<Transaction> convertCsvToList(final Record cmd, final Connection conn) throws DispatchException {
		final ImportErrorManager errors = new ImportErrorManager(conn, cmd.file);
		try {
			// read CSV file
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT data FROM import_data WHERE id=?;", cmd.file);
			try {
				final ResultSet rs = stmt.executeQuery();
				if (!rs.next()) {
					errors.add(0, "no data found");
					return null;
				}
				final CsvReader csv = new CsvReader(rs.getCharacterStream("data"));
		    	int lineNo = 1;
		    	// extract header if any
		    	String[] labels;
				if (cmd.row_header) {
					if (!csv.readHeaders()) {
						errors.add(0, "no data found");
						return null;
					}
					labels = csv.getHeaders();
					final Set<String> allowedLabels = new HashSet<String>(Arrays.asList(header));
					for (int i = 0; i < labels.length; ++i) {
						labels[i] = labels[i].toLowerCase();
						if (!allowedLabels.contains(labels[i])) {
							errors.add(lineNo, "unsupported column '" + labels[i] + "'");
							return null;
						}
					}
					++lineNo;
				} else {
					csv.setHeaders(header);
					labels = header;
				}
				final List<Transaction> ret = new LinkedList<Transaction>();
				final Serializer serializer = new Persister(new SimpleCsvMatcher());
				while (csv.readRecord()) {
					if (csv.getColumnCount() != labels.length) {
						if (!errors.add(lineNo, "unexpected number of columns (got " + csv.getColumnCount() + " expect " + labels.length + ")"))
							return null;
					} else {
						// convert CSV line to an XML record so that it can be converted to a Transaction object
						// also it enables me to get line number for any error otherwise SimpleXML
						// does not support error line number.
						final XmlResponseWriter xml = new XmlResponseWriter();
						xml.beginRecord();
						for (int i = 0; i < labels.length; ++i) {
							final String value = csv.get(i);
							if (!value.isEmpty())
								xml.write(labels[i], value);
						}
						xml.endRecord();
						try {
							// convert XML record to transaction class instance
							ret.add(serializer.read(Transaction.class, xml.toString()));
						} catch (final InvocationTargetException e) {
							if (log.isDebugEnabled())
								log.debug("reflection wrapped an exception thrown during deserialization");
							final Throwable ee = e.getCause();
							if (ee == null) {
								if (log.isErrorEnabled())
									log.error("error while deserializing xml request", e);
								if (!errors.add(lineNo, "", CommonServerErrors.INTERNAL_ERROR.toString()))
									return null;
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
					++lineNo;
				}
				csv.close();
				return errors.isEmpty() ? ret : null;
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("failed to parse CSV data", e);
			Util.throwInternalErrorException(e);
		} finally {
			errors.close();
		}
		return null;
	}

	private static String extractFieldName(final String simpleErrorMessage) {
		final String[] matches = simpleErrorMessage.split("'");
		return (matches.length < 3) ? "?" : matches[1];
	}
*/
}
