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

import com.nabla.wapp.shared.validator.TextLengthConstraint;


/**
 * @author nabla
 *
 */
public interface ISplitAsset {

	static final String					TABLE = IAsset.TABLE;

	static final String					NAME_A = "nameA";
	static final String					NAME_B = "nameB";
	static final TextLengthConstraint		NAME_CONSTRAINT = IAsset.NAME_CONSTRAINT;

	static final String					REFERENCE_A = "referenceA";
	static final String					REFERENCE_B = "referenceB";
	static final TextLengthConstraint		REFERENCE_CONSTRAINT = IAsset.REFERENCE_CONSTRAINT;

	static final String					COST_A = "costA";
	static final String					COST_B = "costB";
	static final String					TOTAL = "total";

}
