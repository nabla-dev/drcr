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
package com.nabla.wapp.shared.validator;

import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
public class IntegerRangeConstraint implements IValueConstraint<Integer> {

	private final Integer	minValue;
	private final Integer	maxValue;

	public IntegerRangeConstraint(final Integer minValue, final Integer maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public IntegerRangeConstraint(final Integer minValue) {
		this.minValue = minValue;
		this.maxValue = null;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	@Override
	public boolean validate(final String field, final Integer value, final IErrorList errors) {
		return validate(field, value, CommonServerErrors.INVALID_VALUE, errors);
	}

	public <E extends Enum<E>> boolean validate(final String field, final Integer value, final E error, final IErrorList errors) {
		if (value == null) {
			errors.add(field, CommonServerErrors.REQUIRED_VALUE);
			return false;
		}
		if (minValue != null && value < minValue) {
			errors.add(field, error);
			return false;
		}
		if (maxValue != null && value > maxValue) {
			errors.add(field, error);
			return false;
		}
		return true;
	}

}
