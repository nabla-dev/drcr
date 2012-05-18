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

/**
 * @author nabla
 *
 */
public class FetchUserDefinition extends AbstractFetch {

	private static final long serialVersionUID = 1L;

	Integer	objectId;
	Integer	userId;
	Integer	parentId;

	protected FetchUserDefinition() {}	// for serialization only

	public FetchUserDefinition(final Integer objectId, final Integer userId, final Integer parentId) {
		this.objectId = objectId;
		this.userId = userId;
		this.parentId = parentId;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public Integer getObjectId() {
		return objectId;
	}

}
