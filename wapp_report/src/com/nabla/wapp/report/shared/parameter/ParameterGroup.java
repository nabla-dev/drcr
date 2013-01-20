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

import java.util.LinkedList;


public class ParameterGroup extends LinkedList<IParameter> implements IParameter {

	private static final long serialVersionUID = 1L;

	private String		name;
	private String		prompt;
	private Boolean		cascading;

	public ParameterGroup() {}	// for serialization only

	public ParameterGroup(final String name, final String prompt, final Boolean cascading) {
		this.name = name;
		this.prompt = prompt;
		this.cascading = cascading;
	}

	public ParameterGroup(final String name, final String prompt) {
		this(name, prompt, false);
	}

	public Boolean getCascading() {
		return cascading;
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
		for (IParameter p : this) {
			if (p.needUserInput())
				return true;
		}
		return false;
	}

}
