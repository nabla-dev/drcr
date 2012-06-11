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
package com.nabla.dc.shared.command.company.settings;

import java.io.Reader;
import java.sql.SQLException;

import com.nabla.wapp.server.csv.CsvReader;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.BatchInsertStatement;

/**
 * @author nabla64
 *
 */
public class AccountCsvReader extends CsvReader<AddAccount> {

	public AccountCsvReader(final Reader reader, final ICsvErrorList errors) {
		super(reader, AddAccount.class, errors);
	}

	public boolean read(final ImportAccountList cmd, final BatchInsertStatement<AddAccount> stmt) throws SQLException {
		if (cmd.isRowHeader() && !readHeader())
			return false;
		final AddAccount record = new AddAccount();
		record.setCompanyId(cmd.getCompanyId());
		record.setActive(true);
		while (true) {
			switch (next(record)) {
			case ERROR:
				if (errors.isFull())
					return false;
				break;
			case SUCCESS:
				stmt.add(record);
				break;
			case EOF:
				return errors.isEmpty();
			}
		}		
	}
}
