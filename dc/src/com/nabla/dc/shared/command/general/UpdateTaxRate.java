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
package com.nabla.dc.shared.command.general;

import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.general.Nullable;

/**
 * @author nabla
 *
 */
public class UpdateTaxRate extends AddTaxRate {

	@IRecordField(id=true)
	int		id;

	UpdateTaxRate() {}	// for serialization only

	public UpdateTaxRate(final int id, @Nullable final String name, @Nullable final Integer rate, @Nullable final Boolean active) {
		super(name, rate, active);
		this.id = id;
	}

}
