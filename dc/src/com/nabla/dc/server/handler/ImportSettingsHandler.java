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
import java.sql.Date;
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
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.shared.command.ImportSettings;
import com.nabla.dc.shared.model.IImportSettings;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.auth.UserManager;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class ImportSettingsHandler extends AbstractHandler<ImportSettings, StringResult> {

	@Root
	static class Role {
		@Element
		String			name;
		@ElementList(entry="role", required=false)
		List<String>	definition;

		@Validate
		public void validate(Map session) throws ValidationException {
			if (IRootUser.NAME.equalsIgnoreCase(name))	// ROOT name not allowed
				throw new ValidationException("name", CommonServerErrors.INVALID_VALUE);
		}

		public boolean save(final UserManager userManager, final ICsvErrorList errors) throws SQLException {
			final Integer id = userManager.addRole(name);
			if (id == null) {
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
				return false;
			}
			return true;
		}
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
		@ElementList(required=false)
		List<Account>				accounts;
		@ElementList(entry="user", required=false)
		List<CompanyUser>			users;

		@Validate
		public void validate() throws PersistenceException {
			if (visible == null)
				visible = false;
			if (asset_categories != null && !asset_categories.isEmpty()) {
				final Set<String> categories = new HashSet<String>();
				for (CompanyAssetCategory category : asset_categories) {
					if (categories.contains(category.asset_category))
						throw new PersistenceException("asset category ''%s'' already defined for company ''%s''", category.asset_category, name);
					categories.add(category.asset_category);
				}
			}
		}
	}

	@Root(name="dc-settings",strict=false)
	static class Settings {

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

		public void clearOldValues(final Connection conn) throws SQLException {
			Database.executeUpdate(conn,
"DELETE FROM company, fa_asset_category, fa_fs_category;");
			Database.executeUpdate(conn,
"DELETE FROM user WHERE name NOT LIKE ?;", IRootUser.NAME);
		}

		public boolean save(final Connection conn, final ICsvErrorList errors) throws SQLException {
			errors.setLine(null);
			final UserManager userManager = new UserManager(conn);
			for (Role role : roles) {
				if (!role.save(userManager, errors) && errors.isFull())
					return false;
			}

			/*					final SqlInsert<AddAccount> sql = new SqlInsert<AddAccount>(AddAccount.class, option);
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
								}*/

			return false;
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

	private boolean add(final ImportSettings cmd, final ICsvErrorList errors, final IUserSessionContext ctx) throws DispatchException, SQLException {
		final Settings settings = new Importer(ctx.getReadConnection(), errors).read(Settings.class, cmd.getBatchId());
		if (settings == null)
			return false;
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			SqlInsertOptions option = cmd.getOverwrite();
			if (option == SqlInsertOptions.REPLACE) {
				settings.clearOldValues(conn);
				option = SqlInsertOptions.OVERWRITE;
			}
			UserPreference.save(ctx, IImportSettings.PREFERENCE_GROUP, IImportSettings.OVERWRITE, cmd.getOverwrite());
			return guard.setSuccess(settings.save(conn, errors));
		} finally {
			guard.close();
		}
	}

}
