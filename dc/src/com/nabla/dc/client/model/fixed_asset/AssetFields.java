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
package com.nabla.dc.client.model.fixed_asset;

import com.nabla.dc.shared.model.fixed_asset.IAsset;

/**
 * @author FNorais
 *
 */
public class AssetFields implements IAsset {
	public String name() { return NAME; }
	public String category() { return CATEGORY; }
	public String reference() { return REFERENCE; }
	public String location() { return LOCATION; }

	public String acquisitionDate() { return ACQUISITION_DATE; }
	public String acquisitionType() { return ACQUISITION_TYPE; }
	public String cost() { return COST; }
	public String pi() { return PURCHASE_INVOICE; }
	public String initialAccumDep() { return INITIAL_ACCUMULATED_DEPRECIATION; }
	public String initialDepPeriod() { return INITIAL_DEPRECIATION_PERIOD; }

	public String depPeriod() { return DEPRECIATION_PERIOD; }
	public String residualValue() { return RESIDUAL_VALUE; }
	public String createTransactions() { return CREATE_TRANSACTIONS; }
	public String opening() { return OPENING; }
	public String openingMonth() { return OPENING_MONTH; }
	public String openingYear() { return OPENING_YEAR; }
	public String openingAccumDep() { return OPENING_ACCUMULATED_DEPRECIATION; }
	public String openingDepPeriod() { return OPENING_DEPRECIATION_PERIOD; }

	public String disposalDate() { return DISPOSAL_DATE; }
	public String disposalType() { return DISPOSAL_TYPE; }
	public String proceeds() { return PROCEEDS; }
}
