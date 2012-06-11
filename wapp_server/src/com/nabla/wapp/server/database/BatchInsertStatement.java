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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.InternalErrorException;

/**
 * @author nabla
 *
 */
public class BatchInsertStatement<T> extends SqlStatement {

	private final SqlInsert<T>			sql;
	private final PreparedStatement 	stmt;

	public BatchInsertStatement(final Connection conn, final SqlInsert<T> sql) throws SQLException {
		this.sql = sql;
		stmt = sql.prepareStatement(conn);
	}

	public void close() {
		Database.close(stmt);
	}
	
	public void add(final T record) throws SQLException {
		Assert.argumentNotNull(record);

		int i = 1;
		for (IStatementParameter parameter : sql.getParameters())
			parameter.write(stmt, i++, record);
		stmt.addBatch();
	}

	public List<Integer> execute() throws SQLException, InternalErrorException {
		if (!Database.isBatchCompleted(stmt.executeBatch()))
			Util.throwInternalErrorException("failed to insert list of records");
		if (!sql.getGenerateKeys())
			return null;
		final ResultSet rs = stmt.getGeneratedKeys();
		try {
			if (rs.next()) {
				final ArrayList<Integer> keys = new ArrayList<Integer>();
				do {
					keys.add(rs.getInt(1));
				} while (rs.next());
				return keys;
			}
			return null;
		} finally {
			rs.close();
		}
	}

}
