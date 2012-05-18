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

package com.nabla.wapp.server.database;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author nabla
 *
 */
public enum SQLState {

	SUCCESS("00"),
	WARNING("01"),
	INVALID_AUTHORIZATION("28"),
	ACCESS_DENIED("42"),
	INTEGRITY_CONSTRAINT_VIOLATION("23"),
	REQUIRED_FIELD("HY", "000", 1364),
	UNKNOWN("?", "?");

	private static final Log		log = LogFactory.getLog(SQLState.class);

	// SQLSTATE value
	private final String			sqlStateGroup;
	private final String			sqlStateSubcode;
	// DB error code
	private final Integer			dbErrorCode;

	SQLState(final String sqlStateGroup, final String sqlStateSubcode, final Integer dbErrorCode) {
		this.sqlStateGroup = sqlStateGroup;
		this.sqlStateSubcode = sqlStateSubcode;
		this.dbErrorCode = dbErrorCode;
	}

	SQLState(final String sqlStateGroup, final String sqlStateSubcode) {
		this(sqlStateGroup, sqlStateSubcode, null);
	}

	SQLState(final String sqlStateGroup) {
		this(sqlStateGroup, "000", null);
	}

	public String getSqlStateGroup() {
		return sqlStateGroup;
	}

	public String getSqlStateSubCode() {
		return sqlStateSubcode;
	}

	public String getSqlState() {
		return sqlStateGroup + sqlStateSubcode;
	}

	public Integer getDbErrorCode() {
		return dbErrorCode;
	}

	public boolean compare(final String sqlState, int errorCode) {
		return getSqlState().equalsIgnoreCase(sqlState) && (this.dbErrorCode == null || this.dbErrorCode == errorCode);
	}

	public static SQLState valueOf(final String sqlState, int dbErrorCode) {
		if (sqlState != null && sqlState.length() == 5) {
			for (SQLState e : SQLState.values()) {
				if (e.compare(sqlState, dbErrorCode))
					return e;
			}
			if (log.isDebugEnabled())
				log.debug(dbErrorCode + "-" + sqlState + " not found!");
		}
		return UNKNOWN;
	}

	public static SQLState valueOf(final SQLException e) {
		return valueOf(e.getSQLState(), e.getErrorCode());
	}

}
