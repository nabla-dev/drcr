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

public class CurrencyValidator extends RegexValidator {

	public static final String	REGEX = "^(-)?\\d+(\\.[\\d]{0,2})?$";

	public CurrencyValidator(boolean stopIfFalse) {
		super(REGEX, stopIfFalse, Application.getInstance().getLocalizedError(CommonServerErrors.INVALID_CURRENCY_VALUE));
		this.setValidateOnChange(true);
	}

	public CurrencyValidator() {
		this(true);
	}

}
