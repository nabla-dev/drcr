package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.auth.UserManager;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root class XmlUser {
		@Element
		XmlUserName		name;
		@Element(required=false)
		XmlUserPassword	password;
		@Element(required=false)
		Boolean			active;
		@ElementList(entry="role", required=false)
		List<XmlRoleName>	roles;

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
				// otherwise user already exists so do nothing
			} else {
				Database.executeUpdate(conn,
"DELETE FROM user_definition WHERE object_id IS NULL AND user_id=;", userId);
				final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO user_definition (user_id, role_id) VALUES(?,?);");
				try {
					stmt.setInt(1, userId);
					int n = errors.size();
					for (XmlRoleName role : roles) {
						final Integer id = roleIds.get(role.getValue());
						if (id == null) {
							errors.setLine(role.getRow());
							errors.add(XmlRoleName.FIELD, CommonServerErrors.INVALID_VALUE);
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