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
package com.nabla.dc.server.xml.assets;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.FullErrorListException;
import com.nabla.wapp.shared.model.IErrorList;

@Root(name="dc-assets")
public class XmlCompanyAssets extends Node {

	private static final Log	log = LogFactory.getLog(XmlCompanyAssets.class);

	Integer			companyId;
	@Element(required=false)
	XmlAssetList	assets;

	public XmlCompanyAssets() {}
/*
	public XmlAssets(final Connection conn) throws SQLException {
		load(conn);
	}
*/
	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();

		final Company company = ctx.getCompany();
		if (company == null)
			errors.add(CommonServerErrors.RECORD_HAS_BEEN_REMOVED);
		else {
			companyId = company.getId();
			if (assets != null)
				assets.postValidate(company, ctx);
		}
	}

	public void clear(final Connection conn) throws SQLException {
		if (log.isDebugEnabled())
			log.debug("deleting all assets for company");
		Database.executeUpdate(conn,
"DELETE fa_asset" +
" FROM fa_asset AS a INNER JOIN fa_company_asset_category AS c ON a.fa_company_asset_category_id=c.id" +
" WHERE a.company_id=?;", companyId);

	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		try {
			return assets == null || assets.save(conn, ctx);
		} catch (FullErrorListException _) {}
		return false;
	}
/*
	public void load(final Connection conn) throws SQLException {
		roles = new XmlRoleList(conn);
		users = new XmlUserList(conn);
		asset_categories = new XmlAssetCategoryList(conn);
		financial_statement_categories = new XmlFinancialStatementCategoryList(conn);
		companies = new XmlCompanyList(conn);
	}
*/
}