/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.server.handler;

import java.sql.SQLException;

import com.nabla.wapp.report.shared.IReport;
import com.nabla.wapp.report.shared.command.GetReportParameterDefaultValues;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.AbstractGetDefaultValuesHandler;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;


public class GetReportParameterDefaultValuesHandler extends AbstractGetDefaultValuesHandler<GetReportParameterDefaultValues> {

	@Override
	public StringResult execute(final GetReportParameterDefaultValues cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (cmd.getParameterNames().size() <= 0)
			return null;
		return executeQuery(StatementFormat.prepare(ctx.getReadConnection(),
"SELECT name AS 'field', state AS 'default'" +
" FROM user_preference" +
" WHERE object_id IS NULL AND user_id=? AND category=? AND name IN (?);",
ctx.getUserId(), IReport.PRINT_REPORT_PREFERENCE_GROUP, cmd.getParameterNames()));
	}

}
