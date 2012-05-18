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

import com.nabla.wapp.client.command.ICommand;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

/**
 * @author nabla
 *
 */
public class FormItemCommand extends AbstractFormItemIcon implements IFormItemIcon {

	public enum Types {
		CLEAR(PickerIcon.CLEAR),
		COMBO_BOX(PickerIcon.COMBO_BOX),
		DATE(PickerIcon.DATE),
		REFRESH(PickerIcon.REFRESH),
		SEARCH(PickerIcon.SEARCH);

		private final PickerIcon.Picker	impl;

		Types(PickerIcon.Picker impl) {
			this.impl = impl;
		}

		public PickerIcon.Picker getImpl() {
			return impl;
		}

	}

	private PickerIcon.Picker	picker = PickerIcon.REFRESH;
	ICommand					cmd = null;

	public FormItemCommand() {
	}

	@Override
	public FormItemIcon getIcon() {
		return initialize(new PickerIcon(picker, new FormItemClickHandler() {
			@Override
			public void onFormItemClick(@SuppressWarnings("unused") FormItemIconClickEvent event) {
				if (cmd != null)
					cmd.fire();
			}
		}));
	}

	public void setIcon(String icon) {
		picker = new PickerIcon.Picker(icon);
	}

	public void setType(Types type) {
		picker = type.getImpl();
	}

	public void setCommand(final ICommand cmd) {
		this.cmd = cmd;
	}

}
