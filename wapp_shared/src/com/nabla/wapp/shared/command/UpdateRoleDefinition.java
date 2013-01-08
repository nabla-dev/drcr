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
package com.nabla.wapp.shared.command;

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.SelectionDelta;
import com.nabla.wapp.shared.model.IErrorList;


public class UpdateRoleDefinition implements IRecordAction<StringResult> {

	Integer			roleId;
	SelectionDelta	selectionDelta;

	protected UpdateRoleDefinition() {}	// for serialization only

	public UpdateRoleDefinition(final Integer roleId, final SelectionDelta selectionDelta) {
		this.roleId = roleId;
		this.selectionDelta = selectionDelta;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public SelectionDelta getSelectionDelta() {
		return selectionDelta;
	}

	@Override
	public boolean validate(@SuppressWarnings("unused") IErrorList<Void> errors) throws DispatchException {
		return true;
	}

}
