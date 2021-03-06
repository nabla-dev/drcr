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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.dc.shared.model.company.IFinancialYear;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.IRowMap;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

@Root
class XmlCompany extends Node {

	private static final Log	log = LogFactory.getLog(XmlCompany.class);

	static final String	NAME = "name";
	static final String FINANCIAL_YEAR = "financial_year";

	@Attribute
	Integer					xmlRowMapId;
	@Element(name=NAME)
	String					name;
	@Element(name="visible", required=false)
	Boolean						active;
	@Element(name=FINANCIAL_YEAR)
	String					financial_year;
	@Element
	Date						start_date;
	@Element(required=false)
	XmlCompanyAssetCategoryList	asset_categories;
	@Element(required=false)
	XmlAccountList				accounts;
	@Element(required=false)
	XmlCompanyUserList			users;

	public XmlCompany() {}

	public XmlCompany(final ResultSet rs) throws SQLException {
		name = rs.getString(2);
		active = rs.getBoolean(3);
		load(rs.getStatement().getConnection(), rs.getInt(1));
	}

	public String getName() {
		return name;
	}

	@Override
	protected void doValidate(final ImportContext ctx) throws DispatchException {
		final IErrorList<Integer> errors = ctx.getErrorList();
		final IRowMap rows = ctx.getRowMap(xmlRowMapId);

		if (ICompany.NAME_CONSTRAINT.validate(NAME, name, errors, ValidatorContext.ADD) &&
			!ctx.getCompanyNameList().add(getName()))
				errors.add(rows.get(NAME), NAME, CommonServerErrors.DUPLICATE_ENTRY);
		IFinancialYear.NAME_CONSTRAINT.validate(FINANCIAL_YEAR, financial_year, errors, ValidatorContext.ADD);
		if (active == null)
			active = false;
	}

	public boolean save(final Connection conn, final Map<String, Integer> companyIds, final SaveContext ctx) throws SQLException, DispatchException {
		Integer companyId = companyIds.get(getName());
		if (companyId != null) {
			if (ctx.getOption() == SqlInsertOptions.APPEND)
				return true;
			Database.executeUpdate(conn,
"UPDATE company SET active=? WHERE id=?;", active, companyId);
			Database.executeUpdate(conn,
"DELETE FROM financial_year WHERE company_id=?;", companyId);
			if (accounts != null) {
				if (log.isDebugEnabled())
					log.debug("deleting all accounts of company '" + getName() + "'");
				accounts.clear(conn, companyId);
			}
			if (asset_categories != null)
				asset_categories.clear(conn, companyId);
			if (users != null)
				users.clear(conn, companyId);
		} else {
			companyId = Database.addRecord(conn,
"INSERT INTO company (name,uname,active) VALUES(?,?,?);",
getName(), getName().toUpperCase(), active);
			if (companyId == null)
				throw new InternalErrorException(Util.formatInternalErrorDescription("failed to insert company"));
			companyIds.put(getName(), companyId);
		}
		final Integer financialYearId = Database.addRecord(conn,
"INSERT INTO financial_year (company_id, name) VALUES(?,?);", companyId, financial_year);
		final PreparedStatement stmt = conn.prepareStatement(
"INSERT INTO period_end (financial_year_id,name,end_date) VALUES(?,?,?);");
		try {
			stmt.setInt(1, financialYearId);
			final Calendar dt = new GregorianCalendar();
			dt.setTime(start_date);
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
				throw new InternalErrorException(Util.formatInternalErrorDescription("fail to insert periods for company '" + getName() + "'"));
		} finally {
			stmt.close();
		}
		if (accounts != null)
			accounts.save(conn, companyId);
		return (asset_categories == null || asset_categories.save(conn, companyId, ctx)) &&
				(users == null || users.save(conn, companyId, ctx));
	}

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
					financial_year = rs.getString(1);
					dt.setTime(rs.getDate(2));
					dt.set(GregorianCalendar.DAY_OF_MONTH, 1);
				} else {
					financial_year = new Integer(dt.get(GregorianCalendar.YEAR)).toString();
				}
				start_date = new Date(dt.getTime().getTime());
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}
}