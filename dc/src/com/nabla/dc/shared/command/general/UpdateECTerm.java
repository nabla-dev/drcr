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
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;


public class UpdateECTerm extends AddECTerm {

	@IRecordField(id=true)
	int		id;

	UpdateECTerm() {}	// for serialization only

	public UpdateECTerm(final int id, @Nullable final String name, @Nullable final Boolean active) {
		super(name, active);
		this.id = id;
	}

	@Override
	public boolean validate(final IErrorList<Void> errors) throws DispatchException {
		return doValidate(errors, ValidatorContext.UPDATE);
	}

}
