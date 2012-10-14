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

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {

        private final int	id;     // i.e. fa_company_asset_category.id
        private final int	minDepreciationPeriod;
        private final int	maxDepreciationPeriod;

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