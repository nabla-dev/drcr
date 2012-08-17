package com.nabla.dc.server.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.dc.shared.model.company.IFinancialYear;
import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.xml.Importer;
import com.nabla.wapp.server.xml.XmlDate;
import com.nabla.wapp.server.xml.XmlString;
import com.nabla.wapp.shared.database.SqlInsertOptions;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;

@Root class XmlCompany {
		@Element
		XmlString					name;
		@Element(name="visible", required=false)
		Boolean						active;
		@Element
		XmlString					financial_year;
		@Element
		XmlDate						start_date;
		@ElementList(entry="asset_category", required=false)
		List<XmlCompanyAssetCategory>	asset_categories;
		@ElementList(required=false)
		List<XmlAccount>				accounts;
		@ElementList(entry="user", required=false)
		List<XmlCompanyUser>			users;

		@Validate
		public void validate(Map session) throws DispatchException {
			final ICsvErrorList errors = Importer.getErrors(session);
			errors.setLine(name.getRow());
			ICompany.NAME_CONSTRAINT.validate("name", name.getValue(), errors);
			errors.setLine(financial_year.getRow());
			IFinancialYear.NAME_CONSTRAINT.validate(IFinancialYear.NAME, financial_year.getValue(), errors);
			if (active == null)
				active = false;
			if (asset_categories != null && !asset_categories.isEmpty()) {
/*				final Set<String> categories = new HashSet<String>();
				for (CompanyAssetCategory category : asset_categories) {
					if (categories.contains(category.asset_category))
						throw new PersistenceException("asset category ''%s'' already defined for company ''%s''", category.asset_category, name);
					categories.add(category.asset_category);
				}*/
			}
		}

		public static void saveAll(final List<XmlCompany> list, final Connection conn, final SqlInsertOptions option, final ICsvErrorList errors) throws SQLException, DispatchException {
			if (validateAll(list, errors)) {

			}
		}

		private static boolean validateAll(final List<XmlCompany> list, final ICsvErrorList errors) throws DispatchException {
			final Set<String> names = new HashSet<String>();
			for (XmlCompany e : list) {
				if (names.contains(e.name.getValue())) {
					errors.setLine(e.name.getRow());
					errors.add("name", CommonServerErrors.DUPLICATE_ENTRY);
				} else
					names.add(e.name.getValue());
			}
			return names.size() == list.size();
		}
	}