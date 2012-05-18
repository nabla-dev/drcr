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

import com.nabla.wapp.shared.validator.IntegerRangeConstraint;


/**
 * @author nabla64
 *
 */
public class IntegerRangeValidator extends com.smartgwt.client.widgets.form.validator.IntegerRangeValidator {

	public IntegerRangeValidator(Integer min, Integer max, boolean stopIfFalse) {
		this.setMin(min);
		this.setMax(max);
		this.setStopIfFalse(stopIfFalse);
	}

	public IntegerRangeValidator(Integer min, Integer max) {
		this(min, max, true);
	}

	public IntegerRangeValidator(final IntegerRangeConstraint constraint) {
		this(constraint.getMinValue(), constraint.getMaxValue());
	}

	public IntegerRangeValidator(final IntegerRangeConstraint constraint, boolean stopIfFalse) {
		this(constraint.getMinValue(), constraint.getMaxValue(), stopIfFalse);
	}

}
