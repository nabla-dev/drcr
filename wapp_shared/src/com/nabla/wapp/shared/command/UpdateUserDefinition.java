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

import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.SelectionDelta;

/**
 * @author nabla
 *
 */
public class UpdateUserDefinition implements IAction<StringResult> {

	Integer			objectId;
	Integer			userId;
	SelectionDelta	selectionDelta;

	protected UpdateUserDefinition() {}	// for serialization only

	public UpdateUserDefinition(final Integer objectId, final Integer userId, final SelectionDelta selectionDelta) {
		this.objectId = objectId;
		this.userId = userId;
		this.selectionDelta = selectionDelta;
	}

	public Integer getUserId() {
		return userId;
	}

	public SelectionDelta getSelectionDelta() {
		return selectionDelta;
	}

	public Integer getObjectId() {
		return objectId;
	}

}
