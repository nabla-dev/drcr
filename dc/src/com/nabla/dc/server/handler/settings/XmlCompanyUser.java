package com.nabla.dc.server.handler.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlCompanyUser {
	@Element
	XmlUserName			name;
	@Element(required=false)
	Boolean				active;
	@ElementList(entry="role", required=false)
	List<XmlRoleName>	roles;

	@Validate
	public void validate() {
		if (active == null)
			active = false;
	}

	public boolean save(final Connection conn, final Integer companyId, final SaveContext ctx) throws SQLException, DispatchException {
		final ICsvErrorList errors = ctx.getErrors();
		final Integer userId = ctx.getUserIds().get(name.getValue());
		if (userId == null) {
			errors.setLine(name.getRow());
			errors.add("name", CommonServerErrors.INVALID_VALUE);
			return false;
		}
		if (active)
			Database.executeUpdate(conn,
"INSERT INTO company_user (company_id,user_id) VALUES(?,?);", companyId, userId);
		if (roles == null || roles.isEmpty())
			return true;
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO user_definition (object_id,user_id, role_id) VALUES(?,?,?);");
		try {
			stmt.setInt(1, companyId);
			stmt.setInt(2, userId);
			boolean success = true;
			for (XmlRoleName role : roles) {
				final Integer id = ctx.getRoleIds().get(role.getValue());
				if (id == null) {
					errors.setLine(role.getRow());
					errors.add(XmlRoleName.FIELD, CommonServerErrors.INVALID_VALUE);
					success = false;
				} else {
					stmt.setInt(3, id);
					stmt.addBatch();
				}
			}
			if (success && !Database.isBatchCompleted(stmt.executeBatch()))
				Util.throwInternalErrorException("failed to insert company user definition");
			return success;
		} finally {
			Database.close(stmt);
		}
	}
}