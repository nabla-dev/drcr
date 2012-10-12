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
package com.nabla.wapp.server.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.LockTableGuard;
import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.auth.IRootUser;
import com.nabla.wapp.shared.general.IntegerSet;
import com.nabla.wapp.shared.general.SelectionDelta;
import com.nabla.wapp.shared.model.IRoleTable;

/**
 * @author nabla
 *
 */
public class UserManager {

	public interface IRoleListProvider {
		Map<String, String[]> get();
	}

	private static final String	LOCK_USER_TABLES = "role WRITE, user WRITE, user_role WRITE, role_definition WRITE, user_definition WRITE";

	private static final Log		log = LogFactory.getLog(UserManager.class);
	private final Connection		conn;

	public UserManager(final Connection conn) {
		Assert.argumentNotNull(conn);
		this.conn = conn;
	}

	public boolean initializeDatabase(final IRoleListProvider roleListProvider, final String rootPassword) throws SQLException {
		Assert.argumentNotNull(roleListProvider);

		final LockTableGuard lock = new LockTableGuard(conn, LOCK_USER_TABLES);
		try {
			if (!Database.isTableEmpty(conn, IRoleTable.TABLE))
				return true;
			if (log.isDebugEnabled())
				log.debug("initializing role tables");
			final Map<String, String[]> roles = roleListProvider.get();
			Assert.state(!roles.containsKey(IRootUser.NAME));
			final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
			try {
				final PreparedStatement stmtRole = conn.prepareStatement(
"INSERT INTO role (name,uname,privilege,internal) VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
				final Map<String, Integer> roleIds = new HashMap<String, Integer>();
				try {
					stmtRole.clearBatch();
					stmtRole.setBoolean(4, true);
					// add privileges and default roles
					for (final Map.Entry<String, String[]> role : roles.entrySet()) {
						stmtRole.setString(1, role.getKey());
						stmtRole.setString(2, role.getKey().toUpperCase());
						stmtRole.setBoolean(3, role.getValue() == null);
						stmtRole.addBatch();
					}
					if (!Database.isBatchCompleted(stmtRole.executeBatch()))
						return false;
					final ResultSet rsKey = stmtRole.getGeneratedKeys();
					try {
						for (final Map.Entry<String, String[]> role : roles.entrySet()) {
							rsKey.next();
							roleIds.put(role.getKey(), rsKey.getInt(1));
						}
					} finally {
						rsKey.close();
					}
				} finally {
					stmtRole.close();
				}
				final PreparedStatement stmtDefinition = conn.prepareStatement(
"INSERT INTO role_definition (role_id,child_role_id) VALUES(?,?);");
				try {
					stmtDefinition.clearBatch();
					for (final Map.Entry<String, String[]> role : roles.entrySet()) {
						final String[] definition = role.getValue();
						if (definition == null)
							continue;
						stmtDefinition.setInt(1, roleIds.get(role.getKey()));
						for (final String child : definition) {
							final Integer childId = roleIds.get(child);
							if (childId == null) {
								if (log.isErrorEnabled())
									log.error("child role '" + child + "' not defined!");
								return false;
							}
							stmtDefinition.setInt(2, childId);
							stmtDefinition.addBatch();
						}
					}
					if (!Database.isBatchCompleted(stmtDefinition.executeBatch()))
						return false;
				} finally {
					stmtDefinition.close();
				}
				// add 'root' user
				Database.executeUpdate(conn,
"INSERT INTO user (name,uname,active,password) VALUES(?,?,TRUE,?);",
IRootUser.NAME, IRootUser.NAME.toUpperCase(), getPasswordEncryptor().encryptPassword(rootPassword));
				return guard.setSuccess(true);
			} finally {
				guard.close();
			}
		} finally {
			lock.close();
		}
	}

	public Integer isValidSession(final String userName, final String password) throws SQLException {
		Assert.argumentNotNull(userName);
		Assert.argumentNotNull(password);

		final PreparedStatement stmt = conn.prepareStatement(
"SELECT id, password FROM user WHERE uname LIKE ? AND active=TRUE;");
		try {
			stmt.setString(1, userName);
			final ResultSet rs = stmt.executeQuery();
			try {
				if (rs.next()) {
					try {
						if (getPasswordEncryptor().checkPassword(password, rs.getString("password")))
							return rs.getInt("id");
					} catch (Throwable x) {
						log.error("failed to check password", x);
						return null;
					}
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		return null;
	}

	public Integer addUser(final String userName, final String password) throws SQLException {
		Assert.argumentNotNull(userName);
		Assert.argumentNotNull(password);

		if (userName.equalsIgnoreCase(IRootUser.NAME))	// ROOT name not allowed
			return null;
		return Database.addUniqueRecord(conn,
"INSERT INTO user (name,uname,password) VALUES(?,?,?);",
userName, userName.toUpperCase(), getPasswordEncryptor().encryptPassword(password));
	}

	public Integer cloneUser(final Integer fromUserId, final String userName, final String password) throws SQLException {
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
		try {
			final Integer id = unguardedCloneUser(fromUserId, userName, password);
			guard.setSuccess();
			return id;
		} finally {
			guard.close();
		}
	}

	public Integer unguardedCloneUser(final Integer fromUserId, final String userName, final String password) throws SQLException {
		final Integer userId = addUser(userName, password);
		if (userId == null)
			return null;
		// copy user definition and roles
		Database.executeUpdate(conn,
"INSERT INTO user_definition (user_id,role_id,object_id)" +
" SELECT ? AS 'user_id', t.role_id, t.object_id" +
" FROM user_definition AS t" +
" WHERE t.user_id=?;",
			userId, fromUserId);
		Database.executeUpdate(conn,
"INSERT INTO user_role (user_id,role_id,object_id)" +
" SELECT ? AS 'user_id', t.role_id, t.object_id" +
" FROM user_role AS t" +
" WHERE t.user_id=?;",
			userId, fromUserId);
		return userId;
	}

	public boolean removeUsers(final IntegerSet userIds) throws SQLException {
		Database.executeUpdate(conn, "UPDATE user SET uname=NULL WHERE id IN (?);", userIds);
		return true;
	}

	public void restoreUsers(final IntegerSet userIds) throws SQLException {
		Assert.argumentNotNull(userIds);

		if (!userIds.isEmpty())
			Database.executeUpdate(conn,
"UPDATE user SET uname=UPPER(name) WHERE id IN (?);", userIds);
	}

	public Integer addRole(final String name) throws SQLException {
		Assert.argumentNotNull(name);

		if (IRootUser.NAME.equalsIgnoreCase(name))	// ROOT name not allowed
			return null;
		return Database.addUniqueRecord(conn,
"INSERT INTO role (name,uname) VALUES(?,?);", name, name.toUpperCase());
	}

	public boolean removeRoles(final IntegerSet roleIds) throws SQLException {
		Assert.argumentNotNull(roleIds);
		Assert.argument(!roleIds.isEmpty());

		final LockTableGuard lock = new LockTableGuard(conn, LOCK_USER_TABLES);
		try {
			final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
			try {
				// delete roles from 'role', 'role_definition', 'user_definition' tables
				Database.executeUpdate(conn, "DELETE FROM role WHERE id IN (?);", roleIds);
				return guard.setSuccess(updateUserRoleTable());
			} finally {
				guard.close();
			}
		} finally {
			lock.close();
		}
	}

	public boolean updateRoleDefinition(final Integer roleId, final SelectionDelta delta) throws SQLException {
		Assert.argumentNotNull(roleId);
		Assert.argumentNotNull(delta);

		final LockTableGuard lock = new LockTableGuard(conn, LOCK_USER_TABLES);
		try {
			final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
			try {
				if (delta.isRemovals())
					Database.executeUpdate(conn,
"DELETE FROM role_definition WHERE role_id=? AND child_role_id IN (?);", roleId, delta.getRemovals());
				if (delta.isAdditions()) {
					final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO role_definition (role_id, child_role_id) VALUES(?,?);");
					try {
						stmt.clearBatch();
						stmt.setInt(1, roleId);
						for (final Integer childId : delta.getAdditions()) {
							stmt.setInt(2, childId);
							stmt.addBatch();
						}
						if (!Database.isBatchCompleted(stmt.executeBatch()))
							return false;
					} finally {
						stmt.close();
					}
				}
				return guard.setSuccess(updateUserRoleTable());
			} finally {
				guard.close();
			}
		} finally {
			lock.close();
		}
	}

	public boolean updateUserDefinition(final Integer objectId, final Integer userId, final SelectionDelta delta) throws SQLException {
		Assert.argumentNotNull(userId);
		Assert.argumentNotNull(delta);

		final LockTableGuard lock = new LockTableGuard(conn, LOCK_USER_TABLES);
		try {
			final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(conn);
			try {
				if (delta.isRemovals()) {
					if (objectId == null)
						Database.executeUpdate(conn,
"DELETE FROM user_definition WHERE object_id IS NULL AND user_id=? AND role_id IN (?);", userId, delta.getRemovals());
					else
						Database.executeUpdate(conn,
"DELETE FROM user_definition WHERE object_id=? AND user_id=? AND role_id IN (?);", objectId, userId, delta.getRemovals());
				}
				if (delta.isAdditions()) {
					final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO user_definition (object_id, user_id, role_id) VALUES(?,?,?);");
					try {
						stmt.clearBatch();
						if (objectId == null)
							stmt.setNull(1, Types.BIGINT);
						else
							stmt.setInt(1, objectId);
						stmt.setInt(2, userId);
						for (final Integer childId : delta.getAdditions()) {
							stmt.setInt(3, childId);
							stmt.addBatch();
						}
						if (!Database.isBatchCompleted(stmt.executeBatch()))
							return false;
					} finally {
						stmt.close();
					}
				}
				return guard.setSuccess(updateUserRoleTable());
			} finally {
				guard.close();
			}
		} finally {
			lock.close();
		}
	}

	public void changeUserPassword(final String userName, final String password) throws SQLException {
		Assert.argumentNotNull(userName);
		Assert.argumentNotNull(password);

		Database.executeUpdate(conn,
"UPDATE user SET password=? WHERE uname LIKE ?;", getPasswordEncryptor().encryptPassword(password), userName);
	}

	public void onUserLogged(final Integer userId) throws SQLException {
		Database.executeUpdate(conn, "UPDATE user SET last_login=now() WHERE id=?;", userId);
	}

	public static PasswordEncryptor getPasswordEncryptor() {
		return new StrongPasswordEncryptor();
	}

	public boolean updateUserRoleTable() throws SQLException {
		final Map<Integer, Map<Integer,Set<Integer>>> userRoles = loadUserRoles();
		Database.executeUpdate(conn, "DELETE FROM user_role;");
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO user_role (object_id, user_id, role_id) VALUES(?,?,?);");
		try {
			stmt.clearBatch();
			for (Map.Entry<Integer, Map<Integer,Set<Integer>>> e : userRoles.entrySet()) {
				if (e.getKey() == null)
					stmt.setNull(1, Types.BIGINT);
				else
					stmt.setInt(1, e.getKey());
				for (Map.Entry<Integer,Set<Integer>> ee : e.getValue().entrySet()) {
					stmt.setInt(2, ee.getKey());
					for (Integer roleId : ee.getValue()) {
						stmt.setInt(3, roleId);
						stmt.addBatch();
					}
				}
			}
			return Database.isBatchCompleted(stmt.executeBatch());
		} finally {
			stmt.close();
		}
	}

	private Map<Integer, Map<Integer,Set<Integer>>> loadUserRoles() throws SQLException {
		final Map<Integer, Map<Integer,Set<Integer>>> userRoles = new HashMap<Integer, Map<Integer,Set<Integer>>>();

		final Statement stmt = conn.createStatement();
		try {
			// load all roles and privileges
			final Map<Integer, Role> roles = new HashMap<Integer, Role>();
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM role;");
			try {
				while (rs.next())
					roles.put(rs.getInt("id"), new Role(rs.getInt("id"), rs.getString("name")));
				// read first level of role tree
				rs = stmt.executeQuery(
"SELECT role_definition.role_id, role_definition.child_role_id" +
" FROM role_definition INNER JOIN role ON role_definition.role_id=role.id" +
" WHERE role.privilege=FALSE;");
				while (rs.next()) {
					final Integer parentId = rs.getInt("role_id");
					final Integer childId = rs.getInt("child_role_id");
					if (childId == parentId) {
						if (log.isErrorEnabled())
							log.error("'role_definition' table contains a recursive branch in role '" + roles.get(parentId).getName() + "'. Skipping branch");
						continue;
					}
					roles.get(parentId).put(childId, roles.get(childId));
				}
				// load user definition
				rs = stmt.executeQuery(
"SELECT user_definition.object_id, user_definition.user_id, user_definition.role_id" +
" FROM user_definition INNER JOIN user ON user_definition.user_id=user.id" +
" WHERE user.uname IS NOT NULL;");
				while (rs.next()) {
					Map<Integer,Set<Integer>> m;
					Integer id = Database.getInteger(rs, "object_id");
					if (userRoles.containsKey(id))
						m = userRoles.get(id);
					else {
						m = new HashMap<Integer,Set<Integer>>();
						userRoles.put(id, m);
					}
					Set<Integer> ids;
					id = rs.getInt("user_id");
					if (m.containsKey(id))
						ids = m.get(id);
					else {
						ids = new HashSet<Integer>();
						m.put(id, ids);
					}
					final Role role = roles.get(rs.getInt("role_id"));
					if (role != null)
						ids.addAll(role.getDefinition());
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
		return userRoles;
	}

}
