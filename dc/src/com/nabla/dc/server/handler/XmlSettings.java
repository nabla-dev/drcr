package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.FullErrorListException;

@Root(name="dc-settings",strict=false) class XmlSettings {

		@ElementList(required=false)
		List<XmlRole>							roles;
		@ElementList(required=false)
		List<XmlUser>							users;
		@ElementList(entry="asset_category", required=false)
		List<XmlAssetCategory>					asset_categories;
		@ElementList(entry="financial_statement_category", required=false)
		List<XmlFinancialStatementCategory>	financial_statement_categories;
		@ElementList(required=false)
		List<XmlCompany>						companies;

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
				for (XmlRole role : roles)
					role.save(conn);
				if (!errors.isEmpty())
					return false;
				final Map<String, Integer> roleIds = getRoleIdList(conn);
				for (XmlRole role : roles)
					role.saveDefinition(conn, roleIds, errors);
				if (!errors.isEmpty())
					return false;
				for (XmlUser user : users)
					user.save(conn, roleIds, option, errors);
				XmlAssetCategory.saveAll(asset_categories, conn, option, errors);
				XmlFinancialStatementCategory.saveAll(financial_statement_categories, conn, option, errors);
				if (!errors.isEmpty())
					return false;
//				Company.saveAll(companies, conn, option, errors);

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