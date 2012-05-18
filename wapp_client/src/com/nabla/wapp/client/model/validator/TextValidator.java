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

import com.nabla.wapp.shared.validator.RegexConstraint;
import com.nabla.wapp.shared.validator.TextLengthConstraint;

/**
 * @author nabla
 *
 */
public class TextValidator extends ValidatorList {

	private static final long serialVersionUID = 1L;

	public TextValidator(final TextLengthConstraint constraint) {
		this.add(new TextLengthValidator(constraint));
	}

	public TextValidator(final RegexConstraint constraint) {
		this.add(new TextLengthValidator(constraint));
		this.add(new RegexValidator(constraint));
	}

}
