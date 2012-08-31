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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.nabla.wapp.server.xml.XmlString;

/**
 * @author nabla
 *
 */
public class XmlStringSetter extends AbstractSimpleSetter {

	@Override
	public int setValue(PreparedStatement stmt, int parameterIndex, Object value) throws SQLException {
		if (value == null)
			stmt.setNull(parameterIndex, Types.VARCHAR);
		else
			stmt.setString(parameterIndex, ((XmlString)value).getValue());
		return 1;
	}

}