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
package com.nabla.dc.server.handler.settings;

import java.sql.SQLException;

import com.nabla.dc.shared.command.settings.UpdateCompanyTaxRate;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.dispatch.AbstractHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.StringResult;

/**
 * @author nabla
 *
 */
public class UpdateCompanyTaxRateHandler extends AbstractHandler<UpdateCompanyTaxRate, StringResult> {

	public UpdateCompanyTaxRateHandler() {
		super(true);
	}

	@Override
	public StringResult execute(final UpdateCompanyTaxRate record, final IUserSessionContext ctx) throws DispatchException, SQLException {
		if (record.getActive()) {
			Database.executeUpdate(ctx.getWriteConnection(),
"INSERT IGNORE INTO company_tax_rate (company_id,tax_rate_id) VALUES(?,?);",
					record.getCompanyId(), record.getTaxRateId());
		} else {
			Database.executeUpdate(ctx.getWriteConnection(),
"DELETE FROM company_tax_rate WHERE company_id=? AND tax_rate_id=?;",
					record.getCompanyId(), record.getTaxRateId());
		}
		return null;
	}

}
