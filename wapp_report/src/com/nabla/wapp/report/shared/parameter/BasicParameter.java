/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.shared.parameter;


/**
 * The <code></code> object is used to
 *
 */
public class BasicParameter implements IParameter {

	private String		name;
	private String		prompt;

	BasicParameter() {}	// for serialization only

	public BasicParameter(final String name, final String prompt) {
		this.name = name;
		this.prompt = prompt;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPrompt() {
		return prompt;
	}

	@Override
	public boolean needUserInput() {
		return true;
	}

}
