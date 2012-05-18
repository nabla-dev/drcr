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
package com.nabla.wapp.client.model.field;

import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;

/**
 * @author nabla64
 *
 */
public class IntegerSpinnerField extends IntegerField {

	private final SpinnerItem	editor = new SpinnerItem();

	public IntegerSpinnerField(final String name, final IntegerRangeConstraint constraint, final FieldAttributes... attributes) {
		this(name, constraint.getMinValue(), constraint, attributes);
	}

	public IntegerSpinnerField(final String name, int defaultValue, final IntegerRangeConstraint constraint, final FieldAttributes... attributes) {
		super(name);
		editor.setMin(constraint.getMinValue());
		editor.setMax(constraint.getMaxValue());
		setEditorType(editor);
	//	setValidators(new IntegerRangeValidator(constraint));
		this.setDefaultValue(defaultValue);
		FieldAttributes.applyAll(this, attributes);
	}

	public void setStep(final Integer value) {
		editor.setStep(value);
	}

}
