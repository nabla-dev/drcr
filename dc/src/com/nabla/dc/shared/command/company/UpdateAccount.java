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
package com.nabla.dc.shared.command.company;

import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
public class UpdateAccount extends BasicAccountAction implements IRecordAction<StringResult> {

	@IRecordField(id=true)
	int		id;

	UpdateAccount() {}	// for serialization only

	public UpdateAccount(final int id, final String code, final String name, final String cc, final String dep, final Boolean bs, final Boolean active) {
		super(code, name, cc, dep, bs, active);
		this.id = id;
	}

	@Override
	public boolean validate(final IErrorList errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.UPDATE);
	}

}
