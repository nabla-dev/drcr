package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlRole {
	@Element
	XmlRoleName		name;
	@ElementList(entry="role", required=false)
	List<XmlRoleName>	definition;

	@Validate
	public void validate(final Map session) throws DispatchException {
		final Set<String> names = Importer.getContext(session);
		Assert.notNull(names);
		if (names.contains(name.getValue())) {
			final ICsvErrorList errors = Importer.getErrors(session);
			errors.setLine(name.getRow());
			errors.add(XmlRoleName.FIELD, CommonServerErrors.DUPLICATE_ENTRY);
		} else
			names.add(name.getValue());
	}

	public boolean save(final Connection conn) throws SQLException {
		return name.save(conn);
	}

	public void saveDefinition(final Connection conn, final Map<String, Integer> roleIds, final ICsvErrorList errors) throws SQLException, DispatchException {
		final Integer roleId = roleIds.get(name.getValue());
		if (roleId == null) {
			errors.setLine(name.getRow());
			errors.add(XmlRoleName.FIELD, CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
		} else {
			Database.executeUpdate(conn,
"DELETE FROM role_definition WHERE role_id=?;", roleId);
			final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO role_definition (role_id, child_role_id) VALUES(?,?);");
			try {
				stmt.setInt(1, roleId);
				int n = errors.size();
				for (XmlRoleName role : definition) {
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
					Util.throwInternalErrorException("failed to insert role definition");
			} finally {
				Database.close(stmt);
			}
		}
	}

}