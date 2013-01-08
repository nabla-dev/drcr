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


public class TextLengthConstraint implements IValueConstraint<String> {

	private final boolean		nullableOnUpdate;
	private final int			minLength;
	private final int			maxLength;

	public TextLengthConstraint(int minLength, int maxLength, boolean nullableOnUpdate) {
		assert minLength <= maxLength;

		this.nullableOnUpdate = nullableOnUpdate;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public boolean isNullableOnUpdate() {
		return nullableOnUpdate;
	}

	@Override
	public <P> boolean validate(final P position, final String field, @Nullable final String value, final IErrorList<P> errors, final ValidatorContext ctx) throws DispatchException {
		if (value == null || value.isEmpty()) {
			if ((ctx != ValidatorContext.UPDATE || !isNullableOnUpdate()) && getMinLength() > 0) {
				errors.add(position, field, CommonServerErrors.REQUIRED_VALUE);
				return false;
			}
		} else {
			int length = value.length();
			if (length < getMinLength()) {
				errors.add(position, field, CommonServerErrors.TEXT_TOO_SHORT);
				return false;
			}
			if (length > getMaxLength()) {
				errors.add(position, field, CommonServerErrors.TEXT_TOO_LONG);
				return false;
			}
		}
		return true;
	}

	@Override
	public <P> boolean validate(final String field, @Nullable final String value, final IErrorList<P> errors, final ValidatorContext ctx) throws DispatchException {
		return validate(null, field, value, errors, ctx);
	}

}
