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
package com.nabla.dc.server.xml.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlRole {
	private static final Log	log = LogFactory.getLog(XmlRole.class);

	@Element
	XmlRoleName				name;
	@ElementList(entry="role", required=false)
	LinkedList<XmlRoleName>	definition;

	public XmlRole() {}

	public XmlRole(final ResultSet rs) throws SQLException {
		name = new XmlRoleName(rs.getString(2));
		load(rs.getStatement().getConnection(), rs.getInt(1));
	}

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

	public void load(final Connection conn, final Integer id) throws SQLException {
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT r.name" +
" FROM role_definition AS d INNER JOIN role AS r ON d.child_role_id=r.id" +
" WHERE d.role_id=?;", id);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				if (rs.next()) {
					definition = new LinkedList<XmlRoleName>();
					do {
						definition.add(new XmlRoleName(rs.getString(1)));
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