package com.nabla.dc.server.xml.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlCompanyUser {
	@Element
	XmlUserName				name;
	@Element(required=false)
	Boolean					active;
	@ElementList(entry="role", required=false)
	LinkedList<XmlRoleName>	roles;

	public XmlCompanyUser() {}

	public XmlCompanyUser(final Integer companyId, final ResultSet rs) throws SQLException {
		name = new XmlUserName(rs.getString(2));
		active = true;
		load(rs.getStatement().getConnection(), rs.getInt(1), companyId);
	}

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

	public void load(final Connection conn, final Integer userId, final Integer companyId) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT r.name" +
" FROM user_definition AS d INNER JOIN role AS r ON d.role_id=r.id" +
" WHERE d.user_id=? AND d.object_id=?;", userId, companyId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (rs.next()) {
					roles = new LinkedList<XmlRoleName>();
					do {
						roles.add(new XmlRoleName(rs.getString(1)));
					} while (rs.next());
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}
}