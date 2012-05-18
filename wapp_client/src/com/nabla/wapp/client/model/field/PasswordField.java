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

import com.nabla.wapp.client.model.validator.TextValidator;
import com.nabla.wapp.client.model.validator.ValidatorList;
import com.nabla.wapp.shared.validator.RegexConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;
import com.smartgwt.client.data.fields.DataSourcePasswordField;
import com.smartgwt.client.widgets.form.validator.Validator;

/**
 * @author nabla64
 *
 */
public class PasswordField extends DataSourcePasswordField {

	public PasswordField(final String name, final FieldAttributes... attributes) {
		super(name);
		FieldAttributes.applyAll(this, attributes);
	}

	public PasswordField(final String name, final Validator validator, final FieldAttributes... attributes) {
		this(name, attributes);
		this.setValidators(validator);
	}

	public PasswordField(final String name, final ValidatorList validators, final FieldAttributes... attributes) {
		this(name, attributes);
		this.setValidators(validators.asArray());
	}

	public PasswordField(final String name, final TextLengthConstraint constraint, final FieldAttributes... attributes) {
		this(name, new TextValidator(constraint), attributes);
	}

	public PasswordField(final String name, final RegexConstraint constraint, final FieldAttributes... attributes) {
		this(name, new TextValidator(constraint), attributes);
	}

}
