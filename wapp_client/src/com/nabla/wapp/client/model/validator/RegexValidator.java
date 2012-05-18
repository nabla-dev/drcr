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
package com.nabla.wapp.client.model.validator;

import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.validator.RegexConstraint;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 * @author nabla64
 *
 */
public class RegexValidator extends RegExpValidator {

	public RegexValidator(final String expression, boolean stopIfFalse, final String errorMessage) {
		this.setExpression(expression);
		this.setErrorMessage(errorMessage);
		this.setStopIfFalse(stopIfFalse);
	}

	public RegexValidator(final String expression) {
		this(expression, true, Application.getInstance().getLocalizedError(CommonServerErrors.INVALID_CHARACTER));
	}

	public RegexValidator(final RegexConstraint constraint) {
		this(constraint.getExpression());
	}

}
