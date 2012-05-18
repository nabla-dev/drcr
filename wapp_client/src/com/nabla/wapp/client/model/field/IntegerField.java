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

import com.nabla.wapp.client.model.validator.ValidatorList;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.widgets.form.validator.Validator;

/**
 * @author nabla64
 *
 */
public class IntegerField extends DataSourceIntegerField {

	public IntegerField(final String name, final FieldAttributes... attributes) {
		super(name);
		FieldAttributes.applyAll(this, attributes);
	}

	public IntegerField(final String name, final Validator validator,  final FieldAttributes... attributes) {
		super(name);
		this.setValidators(validator);
		FieldAttributes.applyAll(this, attributes);
	}

	public IntegerField(final String name, final ValidatorList validators,  final FieldAttributes... attributes) {
		super(name);
		this.setValidators(validators.asArray());
		FieldAttributes.applyAll(this, attributes);
	}

	public IntegerField(final String name, final Integer defaultValue, final FieldAttributes... attributes) {
		this(name, attributes);
		this.setDefaultValue(defaultValue);
	}

	public IntegerField(final String name, final Integer defaultValue, final Validator validator, final FieldAttributes... attributes) {
		this(name, validator, attributes);
		this.setDefaultValue(defaultValue);
	}

	public IntegerField(final String name, final Integer defaultValue, final ValidatorList validators, final FieldAttributes... attributes) {
		this(name, validators, attributes);
		this.setDefaultValue(defaultValue);
	}

	public void setDefaultValue(Integer value) {
		setAttribute(IFieldReservedNames.DEFAULT_VALUE, value);
	}

}
