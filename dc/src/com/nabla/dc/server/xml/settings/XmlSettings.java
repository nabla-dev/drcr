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
package com.nabla.dc.server.xml.settings;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.FullErrorListException;

@Root(name="dc-settings",strict=false)
public class XmlSettings {

	private static final Log	log = LogFactory.getLog(XmlSettings.class);

	@Element(required=false)
	XmlRoleList							roles;
	@Element(required=false)
	XmlUserList							users;
	@Element(required=false)
	XmlAssetCategoryList				asset_categories;
	@Element(required=false)
	XmlFinancialStatementCategoryList	financial_statement_categories;
	@Element(required=false)
	XmlCompanyList						companies;

	public XmlSettings() {}

	public XmlSettings(final Connection conn) throws SQLException {
		load(conn);
	}

	public void clear(final Connection conn) throws SQLException {
		if (companies != null) {
			if (log.isDebugEnabled())
				log.debug("deleting all companies");
			companies.clear(conn);
		}
		if (financial_statement_categories != null) {
			if (log.isDebugEnabled())
				log.debug("deleting all financial statement categories");
			financial_statement_categories.clear(conn);
		}
		if (asset_categories != null) {
			if (log.isDebugEnabled())
				log.debug("deleting all asset categories");
			asset_categories.clear(conn);
		}
		if (users != null) {
			if (log.isDebugEnabled())
				log.debug("deleting all users");
			users.clear(conn);
		}
		if (roles != null) {
			if (log.isDebugEnabled())
				log.debug("deleting all user defined roles");
			roles.clear(conn);
		}
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		try {
			if (roles != null && !roles.save(conn, ctx))
				return false;
			if (users != null && !users.save(conn, ctx))
				return false;
			if (asset_categories != null)
				asset_categories.save(conn, ctx.getOption());
			if (financial_statement_categories != null)
				financial_statement_categories.save(conn, ctx.getOption());
			return companies == null || companies.save(conn, ctx);
		} catch (FullErrorListException _) {}
		return false;
	}

	public void load(final Connection conn) throws SQLException {
		roles = new XmlRoleList(conn);
		users = new XmlUserList(conn);
		asset_categories = new XmlAssetCategoryList(conn);
		financial_statement_categories = new XmlFinancialStatementCategoryList(conn);
		companies = new XmlCompanyList(conn);
	}

}