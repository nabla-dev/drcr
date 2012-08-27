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
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.dc.shared.model.company.IFinancialYear;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.server.xml.XmlNode;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root
class XmlCompany {
	private static final Log	log = LogFactory.getLog(XmlCompany.class);

	@Element
	XmlString					name;
	@Element(name="visible", required=false)
	Boolean						active;
	@Element
	XmlString					financial_year;
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
		name = new XmlString(rs.getString(2));
		active = rs.getBoolean(3);
		load(rs.getStatement().getConnection(), rs.getInt(1));
	}

	public String getName() {
		return name.getValue();
	}

	@Validate
	public void validate(Map session) throws DispatchException {
		final ICsvErrorList errors = XmlNode.getErrorList(session);
		errors.setLine(name.getRow());
		if (ICompany.NAME_CONSTRAINT.validate("name", getName(), errors) &&
			!XmlNode.<ImportContext>getContext(session).getCompanyNameList().add(getName()))
				errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
		errors.setLine(financial_year.getRow());
		IFinancialYear.NAME_CONSTRAINT.validate("financial_year", financial_year.getValue(), errors);
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
				Util.throwInternalErrorException("failed to insert company");
			companyIds.put(getName(), companyId);
		}
		final Integer financialYearId = Database.addRecord(conn,
"INSERT INTO financial_year (company_id, name) VALUES(?,?);", companyId, financial_year.getValue());
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
				Util.throwInternalErrorException("fail to insert periods for company '" + getName() + "'");
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
	}
}