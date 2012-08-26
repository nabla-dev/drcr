package com.nabla.dc.server.handler.settings;

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

}