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
public class RegexConstraint extends TextLengthConstraint {

	private final String	expression;

	public RegexConstraint(int minLength, int maxLength, String expression, boolean nullableOnUpdate) {
		super(minLength, maxLength, nullableOnUpdate);
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	@Override
	public <P> boolean validate(final P position, final String field, @Nullable final String value, final IErrorList<P> errors, final ValidatorContext ctx) throws DispatchException {
		if (!super.validate(position, field, value, errors, ctx))
			return false;
		if (value != null && !value.matches(getExpression())) {
			errors.add(position, field, CommonServerErrors.INVALID_CHARACTER);
			return false;
		}
		return true;
	}

	@Override
	public <P> boolean validate(final String field, @Nullable final String value, final IErrorList<P> errors, final ValidatorContext ctx) throws DispatchException {
		return validate(null, field, value, errors, ctx);
	}

}
