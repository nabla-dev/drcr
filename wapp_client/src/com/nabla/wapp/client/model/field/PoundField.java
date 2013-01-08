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

import com.nabla.wapp.client.model.PoundType;
import com.nabla.wapp.client.model.validator.PoundRangeValidator;
import com.nabla.wapp.client.model.validator.PoundValidator;
import com.nabla.wapp.client.model.validator.ValidatorList;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.nabla.wapp.shared.validator.IntegerRangeConstraint;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.validator.Validator;

public class PoundField extends DataSourceField {

	public PoundField(final String name, final FieldAttributes... attributes) {
		commonConstructor(name, attributes);
		if (!getIsReadOnly())
			setValidators(new PoundValidator(true));
	}

	public PoundField(final String name, final ValidatorList validators, final FieldAttributes... attributes) {
		commonConstructor(name, attributes);
		if (!getIsReadOnly()) {
			final ValidatorList tmp = new ValidatorList();
			tmp.addAll(validators);
			tmp.add(new PoundValidator(true));
			setValidators(tmp.asArray());
		}
	}

	public PoundField(final String name, final Validator validator, final FieldAttributes... attributes) {
		this(name, new ValidatorList(validator), attributes);
	}

	public PoundField(final String name, final IntegerRangeConstraint validator, final FieldAttributes... attributes) {
		this(name, new PoundRangeValidator(validator), attributes);
	}

	public void setDefaultValue(Integer value) {
		setAttribute(IFieldReservedNames.DEFAULT_VALUE, value);
	}

	public boolean getIsReadOnly() {
		return FieldAttributes.getIsReadOnly(this);
	}

	protected void commonConstructor(final String name, final FieldAttributes... attributes) {
		setName(name);
		setType(PoundType.instance);
		FieldAttributes.applyAll(this, attributes);
	}

}
