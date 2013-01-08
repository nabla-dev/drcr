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

import com.smartgwt.client.widgets.form.fields.IntegerItem;


public class SmartGwtIntegerItem extends IntegerItem {

//	private String	keyPressFilter;

	public SmartGwtIntegerItem(String regex) {
		this.setKeyPressFilter(regex);
	/*	this.keyPressFilter = regex;
		addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Object value = event.getValue();
				if (value != null &&
					keyPressFilter != null && !keyPressFilter.isEmpty() &&
					!value.toString().matches(keyPressFilter))
						event.cancel();
			}
		});*/
	}

	public SmartGwtIntegerItem() {
		this("^[0-9+-]*$");
	}
/*
	public String getKeyPressFilter() {
		return keyPressFilter;
	}

	public void setKeyPressFilter(String regex) {
		this.keyPressFilter = regex;
	}*/
}
