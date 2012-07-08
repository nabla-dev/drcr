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
import com.nabla.wapp.shared.dispatch.VoidResult;

/**
 * @author nabla
 *
 */
public class SaveUserPreference implements IAction<VoidResult> {

	private String		group;
	private String		name;
	private String		state;

	public SaveUserPreference() {}

	protected SaveUserPreference(final String group, final String name) {
		this.group = group;
		this.name = name;
	}

	public SaveUserPreference(final String group, final String name, final String state) {
		this(group, name);
		this.state = state;
	}

	public SaveUserPreference(final String group, final String name, final Integer state) {
		this(group, name);
		this.state = state.toString();
	}

	public String getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

}
