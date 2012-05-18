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
public class TextLengthConstraint implements IValueConstraint {

	private final int	minLength;
	private final int	maxLength;

	public TextLengthConstraint(int minLength, int maxLength) {
		assert minLength <= maxLength;

		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void validate(final String field, final String value) throws ValidationException {
		if (value == null || value.isEmpty()) {
			if (getMinLength() > 0)
				throw new ValidationException(field, CommonServerErrors.REQUIRED_VALUE);
		} else {
			int length = value.length();
			if (length < getMinLength())
				throw new ValidationException(field, CommonServerErrors.TEXT_TOO_SHORT);
			if (length > getMaxLength())
				throw new ValidationException(field, CommonServerErrors.TEXT_TOO_LONG);
		}
	}

}
