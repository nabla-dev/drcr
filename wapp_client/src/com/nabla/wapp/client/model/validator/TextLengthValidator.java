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

import com.nabla.wapp.shared.validator.TextLengthConstraint;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

/**
 * @author nabla64
 *
 */
public class TextLengthValidator extends LengthRangeValidator {

	public TextLengthValidator(int min, int max, boolean stopIfFalse) {
		this.setMin(min);
		this.setMax(max);
		this.setStopIfFalse(stopIfFalse);
	}

	public TextLengthValidator(int min, int max) {
		this(min, max, true);
	}

	public TextLengthValidator(final TextLengthConstraint constraint) {
		this(constraint.getMinLength(), constraint.getMaxLength());
	}

	public TextLengthValidator(final TextLengthConstraint constraint, boolean stopIfFalse) {
		this(constraint.getMinLength(), constraint.getMaxLength(), stopIfFalse);
	}

}
