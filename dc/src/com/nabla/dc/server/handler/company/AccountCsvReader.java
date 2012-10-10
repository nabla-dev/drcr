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
package com.nabla.dc.server.handler.company;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import com.nabla.dc.shared.command.company.AddAccount;
import com.nabla.wapp.server.csv.CsvReader;
import com.nabla.wapp.server.database.BatchInsertStatement;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.FullErrorListException;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla64
 *
 */
public class AccountCsvReader extends CsvReader<AddAccount> {

	private final Set<String>	codes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private final Set<String>	names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private final Integer		companyId;

	public AccountCsvReader(final Reader reader, final Connection conn, final Integer companyId, final IErrorList<Integer> errors) throws SQLException {
		super(reader, AddAccount.class, errors);
		this.companyId = companyId;
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT code, name FROM account WHERE uname IS NOT NULL AND company_id=?;", companyId);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				while (rs.next()) {
					codes.add(rs.getString(1));
					names.add(rs.getString(2));
				}
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	public boolean read(final Boolean rowHeader, final BatchInsertStatement<AddAccount> stmt) throws SQLException, DispatchException {
		try {
			if (rowHeader && !readHeader())
				return false;
			final AddAccount record = new AddAccount();
			record.setCompanyId(companyId);
			record.setActive(true);
			while (true) {
				switch (next(record)) {
				case ERROR:
					break;
				case SUCCESS:
					if (codes.contains(record.getCode()))
						errors.add(getLineNumber(), AddAccount.CODE, CommonServerErrors.DUPLICATE_ENTRY);
					else if (names.contains(record.getName()))
						errors.add(getLineNumber(), AddAccount.NAME, CommonServerErrors.DUPLICATE_ENTRY);
					else {
						codes.add(record.getCode());
						names.add(record.getName());
						stmt.add(record);
					}
					break;
				case EOF:
					return errors.isEmpty();
				}
			}
		} catch (FullErrorListException __) {
			return false;
		}
	}
}
