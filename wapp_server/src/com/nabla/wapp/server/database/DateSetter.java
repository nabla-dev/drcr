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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


public class DateSetter extends AbstractSimpleSetter {

	@Override
	public int setValue(PreparedStatement stmt, int parameterIndex, Object value) throws SQLException {
		if (value == null)
			stmt.setNull(parameterIndex, Types.DATE);
		else
			stmt.setDate(parameterIndex, (Date)value);
		return 1;
	}

}
