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

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nabla.wapp.server.database.Database;

/**
 * @author nabla
 *
 */
public class AuthDatabase extends Database {

	private static final Log	log = LogFactory.getLog(AuthDatabase.class);

	public AuthDatabase(final String name, final Class<?> roles, final String rootPassword) throws SQLException {
		super(name);
		final Connection conn = this.getConnection();
		try {
			new UserManager(conn).initializeDatabase(new UserManager.IRoleListProvider() {
				@Override
				public Map<String, String[]> get() {
					final Map<String, String[]> rslt = new HashMap<String, String[]>();
					for (final Field field : roles.getFields()) {
						if (!field.getType().getCanonicalName().equals(String.class.getName()))
							continue;
						try {
							final String roleName = (String)field.get(null);	// assume static
							final IRole definition = field.getAnnotation(IRole.class);
							if (definition == null) {
								// privilege
								rslt.put(roleName, null);
							} else {
								// role
								rslt.put(roleName, definition.value());
							}
						} catch (final IllegalArgumentException e) {
							if (log.isDebugEnabled())
								log.debug("invalid field '" + field.getName() + "' as a role", e);
						} catch (final IllegalAccessException e) {
							if (log.isDebugEnabled())
								log.debug("invalid field '" + field.getName() + "' as a role", e);
						}
					}
					return rslt;
				}
			}, rootPassword);
		} finally {
			try { conn.close(); } catch (final SQLException e) {}
		}
	}

}
