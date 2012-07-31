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

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Validate;
import org.simpleframework.xml.core.ValueRequiredException;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.ImportSettings;
import com.nabla.dc.shared.model.IImportSettings;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.xml.SimpleMatcher;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class ImportSettingsHandler extends AbstractHandler<ImportSettings, StringResult> {

	private static final String		XML_ROW_COL = "[row,col]:[";

	@Root
	static class Role {
		@Element
		String			name;
		@ElementList(entry="role", required=false)
		List<String>	definition;

	}

	@Root
	static class User {
		@Element
		String			name;
		@Element(required=false)
		String			password;
		@Element(required=false)
		Boolean			active;
		@ElementList(entry="role", required=false)
		List<String>	roles;

		@Validate
		public void validate() {
			if (password == null && password.isEmpty())
				password = "password";
			if (active == null)
				active = false;
		}
	}

	@Root
	static class AssetCategory {
		@Element
		String					name;
		@Element(required=false)
		Boolean					visible;
		@Element(required=false)
		FixedAssetCategoryTypes	type;
		@Element
		Integer					min_depreciation_period;
		@Element(required=false)
		Integer					max_depreciation_period;

		@Validate
		public void validate() {
			if (visible == null)
				visible = false;
			if (type == null)
				type = FixedAssetCategoryTypes.TANGIBLE;
			if (max_depreciation_period == null)
				max_depreciation_period = min_depreciation_period;
		}
	}

	@Root
	static class FinancialStatementCategory {
		@Element
		String		name;
		@Element(required=false)
		Boolean		visible;

		@Validate
		public void validate() {
			if (visible == null)
				visible = false;
		}
	}

	@Root
	static class CompanyUser {
		@Element
		String			name;
		@Element(required=false)
		Boolean			active;
		@ElementList(entry="role", required=false)
		List<String>	roles;

		@Validate
		public void validate() {
			if (active == null)
				active = false;
		}
	}

	@Root
	static class CompanyAssetCategory {
		@Attribute
		String		financial_statement_category;
		@Text
		String		asset_category;
	}

	@Root
	static class Account {
		@Element
		String		code;
		@Element
		String		name;
		@Element(required=false)
		Boolean		visible;
		@Element(required=false)
		String		cc;
		@Element(required=false)
		String		dep;
		@Element
		Boolean		bs;

		@Validate
		public void validate() {
			if (visible == null)
				visible = true;
		}
	}

	@Root
	static class Company {
		@Element
		String						name;
		@Element(required=false)
		Boolean						visible;
		@Element
		String						financial_year;
		@Element
		Date						start_date;
		@ElementList(entry="asset_category", required=false)
		List<CompanyAssetCategory>	asset_categories;
		@ElementList(entry="user", required=false)
		List<CompanyUser>			users;
		@ElementList(required=false)
		List<Account>				accounts;

		@Validate
		public void validate() throws PersistenceException {
			if (visible == null)
				visible = false;
			if (asset_categories != null && !asset_categories.isEmpty()) {
				final Set<String>	categories = new HashSet<String>();
				for (CompanyAssetCategory category : asset_categories) {
					if (categories.contains(category.asset_category))
						throw new PersistenceException("company asset category '" + category.asset_category + "' already defined");
					categories.add(category.asset_category);
				}
			}
		}
	}

	@Root(name="dc-settings",strict=false)
	static class Settings {

		@ElementList(entry="privilege", required=false)
		List<String>						privileges;
		@ElementList(required=false)
		List<Role>							roles;
		@ElementList(required=false)
		List<User>							users;
		@ElementList(entry="asset_category", required=false)
		List<AssetCategory>					asset_categories;
		@ElementList(entry="financial_statement_category", required=false)
		List<FinancialStatementCategory>	financial_statement_categories;
		@ElementList(required=false)
		List<Company>						companies;

/*		public boolean validate(final Connection conn, final ImportErrorManager errors) throws SQLException {
		}*/
	}

	static class CurrentXmlLine implements Visitor {

		private final ImportErrorManager	errors;

		public CurrentXmlLine(final ImportErrorManager errors) {
			this.errors = errors;
		}

		@Override
		public void read(@SuppressWarnings("unused") Type arg0, NodeMap<InputNode> node) throws Exception {
			int nodeLine = node.getNode().getPosition().getLine();
			if (nodeLine > errors.getLine())
				errors.setLine(nodeLine);
		}

		@Override
		public void write(@SuppressWarnings("unused") Type arg0, @SuppressWarnings("unused") NodeMap<OutputNode> arg1) throws Exception {
		}

	}

	private static final Log	log = LogFactory.getLog(ImportSettingsHandler.class);
	private final IDatabase		writeDb;

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
		} catch (Throwable e) {
			if (log.isErrorEnabled())
				log.error("failed to parse settings from XML data", e);
			Util.throwInternalErrorException(e);
		} finally {
			errors.close();
		}
		return null;
	}

	private boolean add(final ImportSettings cmd, final ImportErrorManager errors, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final Settings settings = readSettings(cmd.getBatchId(), ctx.getReadConnection(), errors);
		if (settings == null)
			return false;
			/*	final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
				try {
					SqlInsertOptions option = cmd.getOverwrite();
					if (option == SqlInsertOptions.REPLACE) {
						Database.executeUpdate(ctx.getWriteConnection(),
"UPDATE account SET uname=NULL WHERE company_id=?;", cmd.getCompanyId());
						option = SqlInsertOptions.OVERWRITE;
					}
					final SqlInsert<AddAccount> sql = new SqlInsert<AddAccount>(AddAccount.class, option);
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
				}*/
		return true;
	}

	private Settings readSettings(final Integer batchId, final Connection conn, final ImportErrorManager errors) throws DispatchException, SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT content FROM import_data WHERE id=?;", batchId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (!rs.next()) {
					errors.add(ServerErrors.NO_DATA_TO_IMPORT);
					return null;
				}
				final CurrentXmlLine currentLine = new CurrentXmlLine(errors);
				final Serializer serializer = new Persister(new VisitorStrategy(currentLine), new SimpleMatcher());
				try {
					final Settings settings = serializer.read(Settings.class, rs.getBinaryStream("content"));
					return /*settings.validate(conn, errors) ?*/ settings /*: null*/;
				} catch (final InvocationTargetException e) {
					if (log.isDebugEnabled())
						log.debug("reflection wrapped an exception thrown during deserialization");
					final Throwable ee = e.getCause();
					if (ee == null) {
						if (log.isErrorEnabled())
							log.error("error while deserializing xml request", e);
						errors.add(CommonServerErrors.INTERNAL_ERROR);
					} else if (ee.getClass().equals(ValidationException.class)) {
						final Map.Entry<String, String> error = ((ValidationException)ee).getError();
						errors.add(error.getKey(), error.getValue());
					} else {
						if (log.isErrorEnabled())
							log.error("error while deserializing xml request", ee);
						errors.add(ee.getLocalizedMessage());
					}
				} catch (final ValueRequiredException e) {
					if (log.isDebugEnabled())
						log.debug("required value", e);
					errors.add(extractFieldName(e), CommonServerErrors.REQUIRED_VALUE);
				} catch (final PersistenceException e) {
					if (log.isDebugEnabled())
						log.debug("deserialization error", e);
					errors.add(extractFieldName(e), CommonServerErrors.INVALID_VALUE);
				} catch (final Exception e) {
					if (log.isDebugEnabled())
						log.debug("error", e);
					errors.add(e.getLocalizedMessage());
				}
				return null;
			} finally {
				Database.close(rs);
			}
		} finally {
			Database.close(stmt);
		}
	}

	private static String extractFieldName(final PersistenceException e) {
		// looking for ...'field name'...
		final String[] matches = e.getMessage().split("'");
		return (matches.length < 3) ? "?" : matches[1];
	}

	private static Integer extractLine(final PersistenceException e) {
		final String message = e.getMessage();
		// looking for [row,col]:[x,x]
		int from = message.indexOf(XML_ROW_COL);
		if (from < 0)
			return null;
		from += XML_ROW_COL.length();
		int to = message.indexOf(',', from);
		return Integer.valueOf(message.substring(from, to));
	}

}
