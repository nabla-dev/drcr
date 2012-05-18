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
package com.nabla.dc.shared.command.settings;

import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateTaxRate extends AddTaxRate {

	private static final long serialVersionUID = 1L;

	@IRecordField(id=true)
	Integer		id;

	protected UpdateTaxRate() {}	// for serialization only

	public UpdateTaxRate(final Integer id, final String name, final Integer rate, final Boolean active) {
		super(name, rate, active);
		this.id = id;
	}

	@Override
	public void validate() throws ValidationException {
		if (name != null) {
			NAME_CONSTRAINT.validate(NAME, name);
			uname = name.toUpperCase();
		}
	}

}
