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
package com.nabla.dc.shared.model.company;

import com.nabla.wapp.shared.validator.RegexConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;

/**
 * The <code></code> object is used to
 *
 */
public interface IAccount {
	static final String				CODE = "code";
	static final RegexConstraint		CODE_CONSTRAINT = new RegexConstraint(1, 12, "([0-9])([0-9]|/)*");
	static final String				NAME = "name";
	static final TextLengthConstraint	NAME_CONSTRAINT = new TextLengthConstraint(1, 64);
	static final String				ACTIVE = "isActive";
	static final String				COST_CENTRE = "cost_centre";
	static final TextLengthConstraint	CC_CONSTRAINT = new TextLengthConstraint(1, 16);
	static final String				DEPARTMENT = "department";
	static final TextLengthConstraint	DEP_CONSTRAINT = new TextLengthConstraint(1, 16);
	static final String				BALANCE_SHEET = "isBalance_sheet";

	static final String				TABLE = "account";
}
