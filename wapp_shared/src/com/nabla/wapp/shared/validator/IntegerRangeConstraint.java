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
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class IntegerRangeConstraint implements IValueConstraint {

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

	public void validate(final String field, final Integer value) throws ValidationException {
		validate(field, value, CommonServerErrors.INVALID_VALUE);
	}

	public <E extends Enum<E>> void validate(final String field, final Integer value, final E error) throws ValidationException {
		if (value == null)
			throw new ValidationException(field, CommonServerErrors.REQUIRED_VALUE);
		if (minValue != null && value < minValue)
			throw new ValidationException(field, error);
		if (maxValue != null && value > maxValue)
			throw new ValidationException(field, error);
	}

}
