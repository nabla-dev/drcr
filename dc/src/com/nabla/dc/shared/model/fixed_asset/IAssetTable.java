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
package com.nabla.dc.shared.model.fixed_asset;


/**
 * @author FNorais
 *
 */
public interface IAssetTable {
	static final String		TABLE = "fa_asset";

	static final String		CATEGORY_ID = "fa_company_asset_category_id";
	static final String		NAME = "name";
	static final String		REFERENCE = "reference";
	static final String		LOCATION = "location";
	static final String		ACQUISITION_DATE = "acquisition_date";
	static final String		ACQUISITION_TYPE = "acquisition_type";
	static final String		PURCHASE_INVOICE = "purchase_invoice";
	static final String		DEPRECIATION_PERIOD = "depreciation_period";
	static final String		DISPOSAL_DATE = "disposal_date";
	static final String		DISPOSAL_TYPE = "disposal_type";
	static final String		PROCEEDS = "proceeds";
}
