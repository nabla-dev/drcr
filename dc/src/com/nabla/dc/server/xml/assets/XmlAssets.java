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
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.model.FullErrorListException;

@Root(name="dc-assets")
public class XmlAssets {

	@ElementList(entry="company", inline=true, required=false,empty=false)
	List<XmlCompany>	companies;

	public XmlAssets() {}
/*
	public XmlAssets(final Connection conn) throws SQLException {
		load(conn);
	}
*/
	@Commit
	public void commit(Map session) {
		XmlNode.<ImportContext>getContext(session).getCompanyNameList().clear();
	}

	public void clear(final Connection conn) throws SQLException {
		for (XmlCompany e : companies)
			e.clear(conn);
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		try {
			boolean success = true;
			for (XmlCompany e : companies)
				success = e.save(conn, ctx) && success;
			return success;
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