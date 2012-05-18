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
package com.nabla.wapp.client.ui.form;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

/**
 * @author nabla
 *
 */
public class HiddenValue extends UiBinderFormItemSpeudoWidget<HiddenItem> {

	private String	value;

	public HiddenValue() {
		super(new HiddenItem(), false);
	}

	public String getValue() {
		return value;
	}

	protected void setValueAsString(final String value) {
		this.value = value;
	}
}
