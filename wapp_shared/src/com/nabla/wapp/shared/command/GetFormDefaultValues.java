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
import com.nabla.wapp.shared.general.Nullable;


public class GetFormDefaultValues implements IAction<StringResult> {

	private @Nullable Integer	objectId;
	private String				group;

	public GetFormDefaultValues() {}

	public GetFormDefaultValues(@Nullable final Integer ObjectId, final String group) {
		this.objectId = ObjectId;
		this.group = group;
	}

	public GetFormDefaultValues(final String group) {
		this(null, group);
	}

	public @Nullable Integer getObjectId() {
		return objectId;
	}

	public String getGroup() {
		return group;
	}

}
