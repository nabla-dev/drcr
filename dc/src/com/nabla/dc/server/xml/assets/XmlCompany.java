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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

@Root
class XmlCompany extends Node {

	static final String	NAME = "name";

	Integer			companyId;
	@Attribute(name=NAME)
	String			name;
	@Element(required=false)
	XmlAssetList	assets;

	public XmlCompany() {}
/*
	public XmlCompany(final ResultSet rs) throws SQLException {
		name = new XmlString(rs.getString(2));
		active = rs.getBoolean(3);
		load(rs.getStatement().getConnection(), rs.getInt(1));
	}
*/
	public String getName() {
		return name;
	}

	@Override
	protected void doValidate(final ImportContext ctx, final IErrorList<Integer> errors) throws DispatchException {
		final Company company = ctx.getCompany(getName());
		if (company == null)
			errors.add(getRow(), NAME, CommonServerErrors.INVALID_VALUE);
		else if (!ctx.getCompanyNameList().add(getName()))
			errors.add(getRow(), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		else {
			companyId = company.getId();
			if (assets != null)
				assets.postValidate(company, errors);
		}
	}

	public void clear(final Connection conn) throws SQLException {
		Database.executeUpdate(conn,
"DELETE fa_asset" +
" FROM fa_asset AS a INNER JOIN fa_company_asset_category AS c ON a.fa_company_asset_category_id=c.id" +
" WHERE a.company_id=?;", companyId);
	}

	public boolean save(final Connection conn, final SaveContext ctx) throws SQLException, DispatchException {
		return (assets == null) || assets.save(conn, ctx);
	}
/*
	public void load(final Connection conn, final Integer id) throws SQLException {
		asset_categories = new XmlCompanyAssetCategoryList(conn, id);
		accounts = new XmlAccountList(conn, id);
		users = new XmlCompanyUserList(conn, id);
		// find first financial year + period
		final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT f.name, p.end_date" +
" FROM period_end AS p INNER JOIN financial_year AS f ON p.financial_year_id=f.id" +
" WHERE f.company_id=? ORDER BY p.end_date ASC LIMIT 1;", id);
		try {
			final ResultSet rs = stmt.executeQuery();
			try {
				final Calendar dt = new GregorianCalendar();	// i.e. today
				if (rs.next()) {
					financial_year = new XmlString(rs.getString(1));
					dt.setTime(rs.getDate(2));
					dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
				} else {
					financial_year = new XmlString(new Integer(dt.get(GregorianCalendar.YEAR)).toString());
				}
				start_date = new Date(dt.getTime().getTime());
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}*/
}