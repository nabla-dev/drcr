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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author nabla64
 *
 */
public class ImportContext {

	public static class Category {
		private final int		id;	// i.e. fa_company_asset_category.id
		private final int		minDepreciationPeriod;
		private final int		maxDepreciationPeriod;

		public Category(final ResultSet rs) throws SQLException {
			this.id = rs.getInt("id");
			this.minDepreciationPeriod = rs.getInt("min_depreciation_period");
			this.maxDepreciationPeriod = rs.getInt("max_depreciation_period");
		}

		public int getId() {
			return id;
		}

		public int getMinDepreciationPeriod() {
			return minDepreciationPeriod;
		}

		public int getMaxDepreciationPeriod() {
			return maxDepreciationPeriod;
		}

	}

	public static class Company extends HashMap<String, Category>{
		private static final long serialVersionUID = 1L;
	}

	private final Set<String>				names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, Company>		companies = new HashMap<String, Company>();
	private final Set<String>				companyNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	public ImportContext(final Connection conn) throws SQLException {
		final PreparedStatement stmt = conn.prepareStatement(
"SELECT company.name AS 'company', t.id, c.name, c.min_depreciation_period, c.max_depreciation_period" +
" FROM company INNER JOIN (" +
" fa_company_asset_category AS t INNER JOIN fa_asset_cateogry AS c ON t.fa_asset_cateogry_id=c.id" +
") ON company.id=t.company_id" +
" WHERE company.uname IS NOT NULL AND t.active=TRUE AND c.uname IS NOT NULL;");
		try {
			loadCompanies(stmt);
		} finally {
			stmt.close();
		}
	}

	public ImportContext(final Connection conn, final Integer companyId) throws SQLException {
		final PreparedStatement stmt = conn.prepareStatement(
"SELECT company.name AS 'company', t.id, c.name, c.min_depreciation_period, c.max_depreciation_period" +
" FROM company INNER JOIN (" +
" fa_company_asset_category AS t INNER JOIN fa_asset_cateogry AS c ON t.fa_asset_cateogry_id=c.id" +
") ON company.id=t.company_id" +
" WHERE company.id=? AND t.active=TRUE AND c.uname IS NOT NULL;");
		try {
			stmt.setInt(1, companyId);
			loadCompanies(stmt);
		} finally {
			stmt.close();
		}
	}

	public Company getCompany(final String name) {
		return companies.get(name);
	}

	public Company getCompany() {
		return companies.entrySet().iterator().next().getValue();
	}

	public Set<String> getNameList() {
		return names;
	}

	public Set<String> getCompanyNameList() {
		return companyNames;
	}

	private void loadCompanies(final PreparedStatement stmt) throws SQLException {
		final ResultSet rs = stmt.executeQuery();
		try {
			while (rs.next()) {
				final String companyName = rs.getString("company");
				Company company = companies.get(companyName);
				if (company == null) {
					company = new Company();
					companies.put(companyName, company);
				}
				company.put(rs.getString("name"), new Category(rs));
			}
		} finally {
			rs.close();
		}
	}
}