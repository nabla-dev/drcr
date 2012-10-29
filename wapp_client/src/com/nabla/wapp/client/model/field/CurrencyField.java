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

import com.nabla.wapp.client.model.CurrencyType;
import com.nabla.wapp.client.model.validator.CurrencyRangeValidator;
import com.nabla.wapp.client.model.validator.CurrencyValidator;
import com.nabla.wapp.client.model.validator.ValidatorList;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.validator.Validator;

/**
 * @author nabla64
 *
 */
public class CurrencyField extends DataSourceField {

	public CurrencyField(final String name, final FieldAttributes... attributes) {
		setName(name);
		setType(CurrencyType.instance);
		FieldAttributes.applyAll(this, attributes);
	}

	public CurrencyField(final String name, final ValidatorList validators, final FieldAttributes... attributes) {
		this(name, attributes);
		if (!validators.isEmpty()) {
			final ValidatorList tmp = new ValidatorList();
			tmp.addAll(validators);
			tmp.add(new CurrencyValidator(true));
			this.setValidators(tmp.asArray());
		}
	}

	public CurrencyField(final String name, final Validator validator, final FieldAttributes... attributes) {
		this(name, attributes);
		final ValidatorList validators = new ValidatorList();
		validators.add(validator);
		validators.add(new CurrencyValidator(true));
		this.setValidators(validators.asArray());
	}

	public CurrencyField(final String name, final IntegerRangeConstraint validator, final FieldAttributes... attributes) {
		this(name, attributes);
		final ValidatorList validators = new ValidatorList();
		validators.add(new CurrencyRangeValidator(validator));
		validators.add(new CurrencyValidator(true));
		this.setValidators(validators.asArray());
	}

	public void setDefaultValue(Integer value) {
		setAttribute(IFieldReservedNames.DEFAULT_VALUE, value);
	}

}
