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
package com.nabla.dc.server.handler.settings.company;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.nabla.dc.shared.command.settings.AddCompany;
import com.nabla.dc.shared.model.ICompany;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.json.IOdbcToJsonEncoder;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.json.OdbcBooleanToJson;
import com.nabla.wapp.server.json.OdbcIntToJson;
import com.nabla.wapp.server.model.AbstractAddHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class AddCompanyHandler extends AbstractAddHandler<AddCompany> {

	private static final List<IOdbcToJsonEncoder>	columns = new LinkedList<IOdbcToJsonEncoder>();

	static {
		columns.add(new OdbcIntToJson("id"));
		columns.add(new OdbcBooleanToJson("active"));
	}

	@Override
	public StringResult execute(final AddCompany record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		validate(record, ctx);
		// return more info than just ID
		final PreparedStatement stmt = StatementFormat.prepare(ctx.getReadConnection(),
"SELECT id, active FROM company WHERE id=?;", add(record, ctx));
		try {
			final JsonResponse json = new JsonResponse();
			json.putNext(stmt.executeQuery(), columns);
			return json.toStringResult();
		} finally {
			Database.close(stmt);
		}
	}

	@Override
	protected int add(AddCompany record, IUserSessionContext ctx) throws DispatchException, SQLException {
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			final Integer companyId = Database.addUniqueRecord(ctx.getWriteConnection(),
"INSERT INTO company (name,uname) VALUES(?,?);",
record.getName(), record.getName().toUpperCase());
			if (companyId == null)
				throw new ValidationException(ICompany.NAME, CommonServerErrors.DUPLICATE_ENTRY);
			final Integer financialYearId = Database.addRecord(ctx.getWriteConnection(),
"INSERT INTO financial_year (company_id, name) VALUES(?,?);",
companyId, record.getFinancialYear());
			final PreparedStatement stmt = ctx.getWriteConnection().prepareStatement(
"INSERT INTO period_end (financial_year_id,name,end_date) VALUES(?,?,?);");
			try {
				stmt.setInt(1, financialYearId);
				final Calendar dt = new GregorianCalendar();
				dt.setTime(record.getStartDate());
				final SimpleDateFormat financialYearFormat = new SimpleDateFormat("MMM yyyy");
				for (int m = 0; m < 12; ++m) {
					dt.set(GregorianCalendar.DAY_OF_MONTH, dt.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
					final Date end = new Date(dt.getTime().getTime());
					stmt.setString(2, financialYearFormat.format(end));
					stmt.setDate(3, end);
					stmt.addBatch();
					dt.add(GregorianCalendar.MONTH, 1);
				}
				if (!Database.isBatchCompleted(stmt.executeBatch()))
					Util.throwInternalErrorException(CommonServerErrors.INTERNAL_ERROR.toString());
			} finally {
				Database.close(stmt);
			}
			guard.setSuccess();
			return companyId;
		} finally {
			guard.close();
		}
	}

}
