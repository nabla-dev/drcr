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
package com.nabla.wapp.shared.model;

import com.nabla.wapp.shared.validator.TextLengthConstraint;


public interface IUser extends IUserTable {
	static final TextLengthConstraint	NAME_CONSTRAINT = new TextLengthConstraint(1, 64, true);
	static final TextLengthConstraint	PASSWORD_CONSTRAINT = new TextLengthConstraint(8, 32, false);

	static final String				CONFIRM_PASSWORD = "confirm";
}
