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
package com.nabla.dc.shared.model;

import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;

/**
 * The <code></code> object is used to
 *
 */
public interface ITaxRate {

	static final String					NAME = "name";
	static final TextLengthConstraint	NAME_CONSTRAINT = new TextLengthConstraint(1, 32);
	static final String					RATE = "rate";
	static final IntegerRangeConstraint	RATE_CONSTRAINT = new IntegerRangeConstraint(0, 9999);
	static int							RATE_DEFAULT = 0;
	static final String					ACTIVE = "active";

	static final String					TABLE = "tax_rate";
}
