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

import com.nabla.wapp.client.model.CurrencyFormatter;
import com.nabla.wapp.client.ui.Resource;
import com.nabla.wapp.shared.validator.IntegerRangeConstraint;

/**
 * The <code></code> object is used to
 *
 */
public class CurrencyRangeValidator extends IntegerRangeValidator {

	@SuppressWarnings("static-access")
	public CurrencyRangeValidator(Integer min, Integer max, boolean stopIfFalse) {
		super(min, max, stopIfFalse);
		final CurrencyFormatter fmt = new CurrencyFormatter();
		this.setErrorMessage(Resource.instance.messages.currencyRangeErrorMessage(fmt.valueToDisplay(min), fmt.valueToDisplay(max)));
	}

	public CurrencyRangeValidator(Integer min, Integer max) {
		this(min, max, true);
	}

	public CurrencyRangeValidator(final IntegerRangeConstraint constraint) {
		this(constraint.getMinValue(), constraint.getMaxValue());
	}

	public CurrencyRangeValidator(final IntegerRangeConstraint constraint, boolean stopIfFalse) {
		this(constraint.getMinValue(), constraint.getMaxValue(), stopIfFalse);
	}

}
