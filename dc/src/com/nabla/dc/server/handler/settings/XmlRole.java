package com.nabla.dc.server.handler.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlRole {
	private static final Log	log = LogFactory.getLog(XmlRole.class);

	@Element
	XmlRoleName			name;
	@ElementList(entry="role", required=false)
	List<XmlRoleName>	definition;

	public String getName() {
		return name.getValue();
	}

	@Validate
	public void validate(final Map session) throws DispatchException {
		if (!XmlNode.<ImportContext>getContext(session).getNameList().add(getName())) {
			final ICsvErrorList errors = XmlNode.getErrorList(session);
			errors.setLine(name.getRow());
			errors.add(XmlRoleName.FIELD, CommonServerErrors.DUPLICATE_ENTRY);
		}
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		final ICsvErrorList errors = ctx.getErrors();
		if (ctx.getPrivilegeIds().containsKey(getName())) {
			if (log.isDebugEnabled())
				log.debug("role '" + getName() + "' is already defined as a privilege");
			errors.setLine(name.getRow());
			errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
			return false;
		}
		Integer roleId = ctx.getRoleIds().get(getName());
		if (roleId != null) {
			if (ctx.getOption() == SqlInsertOptions.APPEND) {
				if (log.isDebugEnabled())
					log.debug("skipping role '" + getName() + "' (already defined)");
				return true;
			}
			// nothing to update for the actual role record
			// but clear definition before replacing it with new definition
			Database.executeUpdate(conn,
"DELETE FROM role_definition WHERE role_id=?;", roleId);
		} else {
			if (log.isDebugEnabled())
				log.debug("adding role '" + getName() + "'");
			roleId = Database.addRecord(conn,
"INSERT INTO role (name,uname) VALUES(?,?);", getName(), getName().toUpperCase());
			if (roleId == null)
				Util.throwInternalErrorException("failed to insert role '" + getName() + "'");
			ctx.getRoleIds().put(getName(), roleId);
		}
		if (definition == null || definition.isEmpty())
			return true;
		if (log.isDebugEnabled())
			log.debug("saving role definition for '" + getName() + "'");
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO role_definition (role_id, child_role_id) VALUES(?,?);");
		try {
			stmt.setInt(1, roleId);
			boolean success = true;
			for (XmlRoleName role : definition) {
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
				Util.throwInternalErrorException("failed to insert role definition");
			return success;
		} finally {
			Database.close(stmt);
		}
	}

}