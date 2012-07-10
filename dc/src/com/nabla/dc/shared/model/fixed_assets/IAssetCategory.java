/**
* Copyright 2010 nabla
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
package com.nabla.dc.shared.model.fixed_assets;

import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;


/**
 * @author nabla
 *
 */
public interface IAssetCategory {

	static final String					NAME = "name";
	static final TextLengthConstraint	NAME_CONSTRAINT = new TextLengthConstraint(1, 128);

	static final String					ACTIVE = "active";
	static final String					TYPE = "type";

	static final String					MIN_DEP_PERIOD = "min_dep_period";
	static final String					MAX_DEP_PERIOD = "max_dep_period";
	static final IntegerRangeConstraint	DEP_PERIOD_CONSTRAINT = new IntegerRangeConstraint(12, 12 * 100);
	static final int					DEFAULT_DEP_PERIOD = 5 * 12;

	static final String					REPORT = "ASSET_CATEGORY_LIST";

	static final String					PREFERENCE_GROUP = "asset_category";
}
