package com.nabla.dc.server.handler.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

@Root
class XmlUser {
	private static final Log	log = LogFactory.getLog(XmlUser.class);

	@Element
	XmlUserName			name;
	@Element
	XmlUserPassword		password;
	@Element(required=false)
	Boolean				active;
	@ElementList(entry="role", required=false)
	List<XmlRoleName>	roles;

	public String getName() {
		return name.getValue();
	}

	@Validate
	public void validate() {
		if (active == null)
			active = false;
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		Integer userId = ctx.getUserIds().get(getName());
		if (userId != null) {
			if (ctx.getOption() == SqlInsertOptions.APPEND) {
				if (log.isDebugEnabled())
					log.debug("skipping user '" + getName() + "' (already defined)");
				return true;
			}
			Database.executeUpdate(conn,
"UPDATE user SET password=?,active=? WHERE id=?;",
UserManager.getPasswordEncryptor().encryptPassword(password.getValue()), active, userId);
			Database.executeUpdate(conn,
"DELETE FROM user_definition WHERE object_id IS NULL AND user_id=?;", userId);
		} else {
			if (log.isDebugEnabled())
				log.debug("adding user '" + getName() + "'");
			userId = Database.addRecord(conn,
"INSERT INTO user (name,uname,active,password) VALUES(?,?,?,?);",
getName(), getName().toUpperCase(), active, UserManager.getPasswordEncryptor().encryptPassword(password.getValue()));
			if (userId == null)
				Util.throwInternalErrorException("failed to insert user");
			ctx.getUserIds().put(getName(), userId);
		}
		if (roles == null || roles.isEmpty())
			return true;
		if (log.isDebugEnabled())
			log.debug("saving user definition for '" + getName() + "'");
		final ICsvErrorList errors = ctx.getErrors();
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO user_definition (user_id, role_id) VALUES(?,?);");
		try {
			stmt.setInt(1, userId);
			boolean success = true;
			for (XmlRoleName role : roles) {
				final Integer id = ctx.getRoleIds().get(role.getValue());
				if (id == null) {
					errors.setLine(role.getRow());
					errors.add(XmlRoleName.FIELD, CommonServerErrors.INVALID_VALUE);
					success = false;
				} else {
					stmt.setInt(2, id);
					stmt.addBatch();
				}
			}
			if (success && !Database.isBatchCompleted(stmt.executeBatch()))
				Util.throwInternalErrorException("failed to insert user definition");
			return success;
		} finally {
			Database.close(stmt);
		}
	}
}