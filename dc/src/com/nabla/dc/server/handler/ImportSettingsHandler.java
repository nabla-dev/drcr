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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import com.google.inject.Inject;
import com.nabla.dc.server.ImportErrorManager;
import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.command.ImportSettings;
import com.nabla.dc.shared.model.IImportSettings;
import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.dc.shared.model.company.IFinancialYear;
import com.nabla.dc.shared.model.fixed_asset.FixedAssetCategoryTypes;
import com.nabla.dc.shared.model.fixed_asset.IFinancialStatementCategory;
import com.nabla.dc.shared.model.fixed_asset.IFixedAssetCategory;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.auth.UserManager;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.IDatabase;
import com.nabla.wapp.server.database.IReadWriteDatabase;
import com.nabla.wapp.server.database.SqlInsert;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.FullErrorListException;
import com.nabla.wapp.shared.model.IRole;
import com.nabla.wapp.shared.model.IUser;

/**
 * @author nabla
 *
 */
public class ImportSettingsHandler extends AbstractHandler<ImportSettings, StringResult> {

	@Root
	static class RoleName extends XmlString {

		public static final String FIELD = "name";

		@Override
		protected void doValidate(final ICsvErrorList errors) throws DispatchException {
			super.doValidate(errors);
			if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
				errors.add(FIELD, CommonServerErrors.INVALID_VALUE);
			IRole.NAME_CONSTRAINT.validate(FIELD, value, errors);
		}

		public void save(final Connection conn) throws SQLException {
			Database.executeUpdate(conn,
"INSERT IGNORE INTO role (name,uname) VALUES(?,?);", value, value.toUpperCase());
		}
	}

	@Root
	static class Role {
		@Element
		RoleName		name;
		@ElementList(entry="role", required=false)
		List<RoleName>	definition;

		public void save(final Connection conn) throws SQLException {
			name.save(conn);
		}

		public void saveDefinition(final Connection conn, final Map<String, Integer> roleIds, final ICsvErrorList errors) throws SQLException, DispatchException {
			final Integer roleId = roleIds.get(name.getValue());
			if (roleId == null) {
				errors.setLine(name.getRow());
				errors.add(RoleName.FIELD, CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
			} else {
				Database.executeUpdate(conn,
"DELETE FROM role_definition WHERE role_id=?;", roleId);
				final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO role_definition (role_id, child_role_id) VALUES(?,?);");
				try {
					stmt.setInt(1, roleId);
					int n = errors.size();
					for (RoleName role : definition) {
						final Integer id = roleIds.get(role.getValue());
						if (id == null) {
							errors.setLine(role.getRow());
							errors.add(RoleName.FIELD, CommonServerErrors.INVALID_VALUE);
						} else {
							stmt.setInt(2, id);
							stmt.addBatch();
						}
					}
					if (n == errors.size() && !Database.isBatchCompleted(stmt.executeBatch()))
						Util.throwInternalErrorException("failed to insert role definition");
				} finally {
					Database.close(stmt);
				}
			}
		}
	}

	@Root
	static class UserName {

		public static final String FIELD = "name";

		@Text
		String	value;

		public String getValue() {
			return value;
		}

		@Validate
		public void validate(Map session) throws DispatchException {
			final ICsvErrorList errors = Importer.getErrors(session);
			if (IRootUser.NAME.equalsIgnoreCase(value))	// ROOT name not allowed
				errors.add(FIELD, CommonServerErrors.INVALID_VALUE);
			IUser.NAME_CONSTRAINT.validate(FIELD, value, errors);
		}

	}

	@Root
	static class UserPassword {

		public static final String FIELD = "password";

		@Text
		String	value;

		public String getValue() {
			return value;
		}

		@Validate
		public void validate(Map session) throws DispatchException {
			IUser.PASSWORD_CONSTRAINT.validate(FIELD, value, Importer.getErrors(session));
		}

	}

	@Root
	static class User {
		@Element
		XmlString		name;
		@Element(required=false)
		XmlString		password;
		@Element(required=false)
		Boolean			active;
		@ElementList(entry="role", required=false)
		List<RoleName>	roles;

		@Validate
		public void validate() {
			if (active == null)
				active = false;
		}

		public void save(final Connection conn, final Map<String, Integer> roleIds, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
			final Integer userId = Database.addRecord(conn,
"INSERT INTO user (name,uname,active,password) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE password=VALUES(password),active=VALUES(active);",
					name.getValue(), name.getValue().toUpperCase(), active, UserManager.getPasswordEncryptor().encryptPassword(password.getValue()));
			if (userId == null) {
				if (option != SqlInsertOptions.APPEND)
					Util.throwInternalErrorException("failed to insert user");
				// user already exists so do nothing
			} else {
				Database.executeUpdate(conn,
"DELETE FROM user_definition WHERE object_id IS NULL AND user_id=;", userId);
				final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO user_definition (user_id, role_id) VALUES(?,?);");
				try {
					stmt.setInt(1, userId);
					int n = errors.size();
					for (RoleName role : roles) {
						final Integer id = roleIds.get(role.getValue());
						if (id == null) {
							errors.setLine(role.getRow());
							errors.add(RoleName.FIELD, CommonServerErrors.INVALID_VALUE);
						} else {
							stmt.setInt(2, id);
							stmt.addBatch();
						}
					}
					if (n == errors.size() && !Database.isBatchCompleted(stmt.executeBatch()))
						Util.throwInternalErrorException("failed to insert user definition");
				} finally {
					Database.close(stmt);
				}
			}
		}
	}

	@Root
	@IRecordTable(name=IFixedAssetCategory.TABLE)
	static class AssetCategory implements IFixedAssetCategory {
		@Element
		@IRecordField
		XmlString				name;
		@IRecordField
		String					uname;
		@Element(name="active",required=false)
		@IRecordField
		Boolean					active;
		@Element(required=false)
		@IRecordField
		FixedAssetCategoryTypes	type;
		@Element
		@IRecordField
		Integer					min_depreciation_period;
		@Element(required=false)
		@IRecordField
		Integer					max_depreciation_period;

		@Validate
		public void validate(Map session) throws DispatchException {
			final ICsvErrorList errors = Importer.getErrors(session);
			errors.setLine(name.getRow());
			if (NAME_CONSTRAINT.validate("name", name.getValue(), errors))
				uname = name.getValue().toUpperCase();
			DEPRECIATION_PERIOD_CONSTRAINT.validate(MIN_DEPRECIATION_PERIOD, min_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors);
			if (max_depreciation_period == null)
				max_depreciation_period = min_depreciation_period;
			else if (DEPRECIATION_PERIOD_CONSTRAINT.validate(MAX_DEPRECIATION_PERIOD, max_depreciation_period, ServerErrors.INVALID_DEPRECIATION_PERIOD, errors) &&
				max_depreciation_period < min_depreciation_period)
					errors.add(MAX_DEPRECIATION_PERIOD, ServerErrors.INVALID_MAX_DEPRECIATION_PERIOD);
			if (active == null)
				active = false;
			if (type == null)
				type = FixedAssetCategoryTypes.TANGIBLE;
		}

		public static void saveAll(final List<AssetCategory> list, final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
			if (validateAll(list, errors)) {
				final SqlInsert<AssetCategory> sql = new SqlInsert<AssetCategory>(AssetCategory.class, option);
				final BatchInsertStatement<AssetCategory> batch = new BatchInsertStatement<AssetCategory>(conn, sql);
				try {
					for (AssetCategory e : list)
						batch.add(e);
					batch.execute();
				} finally {
					batch.close();
				}
			}
		}

		private static boolean validateAll(final List<AssetCategory> list, final ICsvErrorList errors) throws DispatchException {
			final Set<String> names = new HashSet<String>();
			for (AssetCategory e : list) {
				if (names.contains(e.name.getValue())) {
					errors.setLine(e.name.getRow());
					errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
				} else
					names.add(e.name.getValue());
			}
			return names.size() == list.size();
		}
	}

	@Root
	@IRecordTable(name=IFinancialStatementCategory.TABLE)
	static class FinancialStatementCategory implements IFinancialStatementCategory {
		@Element
		@IRecordField
		XmlString	name;
		@IRecordField
		String		uname;
		@Element(name="visible", required=false)
		@IRecordField
		Boolean		active;

		@Validate
		public void validate(Map session) throws DispatchException {
			final ICsvErrorList errors = Importer.getErrors(session);
			errors.setLine(name.getRow());
			if (NAME_CONSTRAINT.validate(NAME, name.getValue(), errors))
				uname = name.getValue().toUpperCase();
			if (active == null)
				active = false;
		}

		public static void saveAll(final List<FinancialStatementCategory> list, final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
			if (validateAll(list, errors)) {
				final SqlInsert<FinancialStatementCategory> sql = new SqlInsert<FinancialStatementCategory>(FinancialStatementCategory.class, option);
				final BatchInsertStatement<FinancialStatementCategory> batch = new BatchInsertStatement<FinancialStatementCategory>(conn, sql);
				try {
					for (FinancialStatementCategory e : list)
						batch.add(e);
					batch.execute();
				} finally {
					batch.close();
				}
			}
		}

		private static boolean validateAll(final List<FinancialStatementCategory> list, final ICsvErrorList errors) throws DispatchException {
			final Set<String> names = new HashSet<String>();
			for (FinancialStatementCategory e : list) {
				if (names.contains(e.name.getValue())) {
					errors.setLine(e.name.getRow());
					errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
				} else
					names.add(e.name.getValue());
			}
			return names.size() == list.size();
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
		XmlString					name;
		@Element(name="visible", required=false)
		Boolean						active;
		@Element
		XmlString					financial_year;
		@Element
		Date						start_date;
		@ElementList(entry="asset_category", required=false)
		List<CompanyAssetCategory>	asset_categories;
		@ElementList(required=false)
		List<Account>				accounts;
		@ElementList(entry="user", required=false)
		List<CompanyUser>			users;

		@Validate
		public void validate(Map session) throws DispatchException {
			final ICsvErrorList errors = Importer.getErrors(session);
			errors.setLine(name.getRow());
			ICompany.NAME_CONSTRAINT.validate("name", name.getValue(), errors);
			errors.setLine(financial_year.getRow());
			IFinancialYear.NAME_CONSTRAINT.validate(IFinancialYear.NAME, financial_year.getValue(), errors);
			if (startDate == null)
				errors.add("start_date", CommonServerErrors.REQUIRED_VALUE);
			if (active == null)
				active = false;
			if (asset_categories != null && !asset_categories.isEmpty()) {
				final Set<String> categories = new HashSet<String>();
				for (CompanyAssetCategory category : asset_categories) {
					if (categories.contains(category.asset_category))
						throw new PersistenceException("asset category ''%s'' already defined for company ''%s''", category.asset_category, name);
					categories.add(category.asset_category);
				}
			}
		}

		public static void saveAll(final List<Company> list, final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {

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
			if (!asset_categories.isEmpty())
				Database.executeUpdate(conn, "DELETE FROM fa_asset_category;");
			if (!financial_statement_categories.isEmpty())
				Database.executeUpdate(conn, "DELETE FROM fa_fs_category;");
			if (!companies.isEmpty())
				Database.executeUpdate(conn, "DELETE FROM company;");
			if (!users.isEmpty())
				Database.executeUpdate(conn,
"DELETE FROM user WHERE name NOT LIKE ?;", IRootUser.NAME);
		}

		public boolean save(final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
			try {
				for (Role role : roles)
					role.save(conn);
				if (!errors.isEmpty())
					return false;
				final Map<String, Integer> roleIds = getRoleIdList(conn);
				for (Role role : roles)
					role.saveDefinition(conn, roleIds, errors);
				if (!errors.isEmpty())
					return false;
				for (User user : users)
					user.save(conn, roleIds, option, errors);
				AssetCategory.saveAll(asset_categories, conn, option, errors);
				FinancialStatementCategory.saveAll(financial_statement_categories, conn, option, errors);
				if (!errors.isEmpty())
					return false;
				Company.saveAll(companies, conn, option, errors);

			} catch (FullErrorListException _) {}
			return false;
		}

		private Map<String, Integer> getRoleIdList(final Connection conn) throws SQLException {
			final Map<String, Integer> roleIds = new HashMap<String, Integer>();
			final Statement stmt = conn.createStatement();
			try {
				final ResultSet rs = stmt.executeQuery(
"SELECT id, name FROM role;");
				try {
					rs.next();
					roleIds.put(rs.getString(2), rs.getInt(1));
				} finally {
					Database.close(rs);
				}
			} finally {
				Database.close(stmt);
			}
			return roleIds;
		}
	}

//	private static final Log	log = LogFactory.getLog(ImportSettingsHandler.class);
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
			Util.throwInternalErrorException(e);
		} finally {
			errors.close();
		}
		return null;
	}

	private boolean add(final ImportSettings cmd, final ICsvErrorList errors, final IUserSessionContext ctx) throws DispatchException, SQLException {
		UserPreference.save(ctx, IImportSettings.PREFERENCE_GROUP, IImportSettings.OVERWRITE, cmd.getOverwrite());
		final Settings settings = new Importer(ctx.getReadConnection(), errors).read(Settings.class, cmd.getBatchId());
		if (settings == null || !errors.isEmpty())
			return false;
		final Connection conn = ctx.getWriteConnection();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			SqlInsertOptions option = cmd.getOverwrite();
			if (option == SqlInsertOptions.REPLACE) {
				settings.clearOldValues(conn);
				option = SqlInsertOptions.INSERT;
			}
			return guard.setSuccess(settings.save(conn, option, errors));
		} finally {
			guard.close();
		}
	}

}
