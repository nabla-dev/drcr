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

import java.sql.ResultSet;
import java.sql.SQLException;

import net.minidev.json.JSONObject;

/**
 * The <code></code> object is used to
 *
 */
public abstract class AbstractOdbcToJsonEncoder implements IOdbcToJsonEncoder {

	protected final String	label;

	public AbstractOdbcToJsonEncoder(final String label) {
		this.label = label;
	}

	@Override
	public void encode(ResultSet rs, @SuppressWarnings("unused") int column, JSONObject record) throws SQLException {
		if (rs.wasNull())
			record.put(label, null);
	}

}
