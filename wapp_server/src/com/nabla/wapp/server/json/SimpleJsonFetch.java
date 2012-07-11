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
package com.nabla.wapp.server.json;

import java.sql.Connection;
import java.sql.SQLException;

import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.FetchResult;


/**
 * The <code></code> object is used to
 *
 */
public class SimpleJsonFetch extends JsonFetch {

	private static final long serialVersionUID = 1L;

	private final String	baseSql;

	public SimpleJsonFetch(final String baseSql) {
		super();
		this.baseSql = baseSql;
	}

	public SimpleJsonFetch(final String baseSql, IOdbcToJsonEncoder... encoders) {
		super(encoders);
		this.baseSql = baseSql;
	}

	public String createSql(final AbstractFetch options) {
		return super.createSql(options, baseSql);
	}

	public FetchResult serialize(final AbstractFetch options, final Connection conn, Object... parameters) throws SQLException {
		return super.serialize(options, conn, baseSql, parameters);
	}

	public FetchResult fetch(final AbstractFetch options, final Connection conn, Object... parameters) throws SQLException {
		return super.fetch(options, conn, baseSql, parameters);
	}

}
