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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.FullErrorListException;

@Root(name="dc-assets")
public class XmlCompanyAssets {

	private static final Log	log = LogFactory.getLog(XmlCompanyAssets.class);

	@Element(required=false)
	XmlAssetList	assets;

	public XmlCompanyAssets() {}
/*
	public XmlAssets(final Connection conn) throws SQLException {
		load(conn);
	}
*/
	@Validate
	public void validate(Map session) throws DispatchException {
		if (assets != null) {
			final ImportContext ctx = XmlNode.getContext(session);
			assets.postValidate(ctx.getCompany(), XmlNode.getErrorList(session));
		}
	}

	public void clear(final Connection conn) throws SQLException {
		if (assets != null) {
			if (log.isDebugEnabled())
				log.debug("deleting all assets for company");
			assets.clear(conn);
		}
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