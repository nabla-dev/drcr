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

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author nabla
 *
 */
public class IntegerRangeConstraint implements IValueConstraint<Integer> {

	private final boolean				nullableOnUpdate;
	@Nullable private final Integer	minValue;
	@Nullable private final Integer	maxValue;

	public IntegerRangeConstraint(@Nullable final Integer minValue, @Nullable final Integer maxValue, final boolean nullableOnUpdate) {
		this.nullableOnUpdate = nullableOnUpdate;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
/*
	public IntegerRangeConstraint(final Integer minValue, final Integer maxValue) {
		this(minValue, maxValue, true);
	}
*/
	public IntegerRangeConstraint(@Nullable final Integer minValue, final boolean nullableOnUpdate) {
		this(minValue, null, nullableOnUpdate);
	}
/*
	public IntegerRangeConstraint(final Integer minValue) {
		this(minValue, true);
	}
*/
	public @Nullable Integer getMinValue() {
		return minValue;
	}

	public @Nullable Integer getMaxValue() {
		return maxValue;
	}

	public boolean isNullableOnUpdate() {
		return nullableOnUpdate;
	}

	@Override
	public boolean validate(final String field, @Nullable final Integer value, final IErrorList errors, final ValidatorContext ctx) throws DispatchException {
		return validate(field, value, CommonServerErrors.INVALID_VALUE, errors, ctx);
	}

	public <E extends Enum<E>> boolean validate(final String field, @Nullable final Integer value, final E error, final IErrorList errors, final ValidatorContext ctx) throws DispatchException {
		if (value == null) {
			if (ctx != ValidatorContext.UPDATE || !isNullableOnUpdate()) {
				errors.add(field, CommonServerErrors.REQUIRED_VALUE);
				return false;
			}
		} else if (getMinValue() != null && value < getMinValue()) {
			errors.add(field, error);
			return false;
		} else 	if (getMaxValue() != null && value > getMaxValue()) {
			errors.add(field, error);
			return false;
		}
		return true;
	}

}
