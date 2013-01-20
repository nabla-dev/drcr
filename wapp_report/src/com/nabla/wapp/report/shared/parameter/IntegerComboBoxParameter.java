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

import java.util.Map;

public class IntegerComboBoxParameter extends BasicParameter {

	private IntegerParameterValueMap		valueMap;

	IntegerComboBoxParameter() {}	// for serialization only

	public IntegerComboBoxParameter(final String name, final String prompt, final IntegerParameterValueMap valueMap) {
		super(name, prompt);
		this.valueMap = valueMap;
	}

	public Map getValueMap() {
		return valueMap.get();
	}
}
