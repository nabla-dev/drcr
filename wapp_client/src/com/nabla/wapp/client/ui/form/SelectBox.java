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

import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.Model;
import com.nabla.wapp.client.model.field.SelectBoxField;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.fields.FormItem;


public class SelectBox extends UiBinderFormTextItemSpeudoWidget<SmartGwtSelectItem> {

	private static final Logger	logger = LoggerFactory.getLog(SelectBox.class);

	public SelectBox() {
		super(new SmartGwtSelectItem(), true);
	}

	@Override
	public FormItem getItem(final Model model) {
		if (model != null) {
			final DataSourceField field = model.getField(impl.getName());
			if (field != null) {
				if (field instanceof SelectBoxField) {
					logger.info("setting model for pick list of '" + impl.getName() + "'");
					final SelectBoxField cbField = (SelectBoxField)field;
					impl.setOptionDataSource(cbField.getModel());
					impl.setValueField(cbField.getValueField());
					impl.setDisplayField(cbField.getDisplayField());
					// add following in order to invalidate pick list and force a call to server
					impl.addCriteria(cbField);
				}
			}
		}
		return super.getItem(model);
	}

	public void setModel(Model model) {
		impl.setOptionDataSource(model);
	}

	public void setValueField(final String name) {
		impl.setValueField(name);
	}

	public void setDisplayField(final String name) {
		impl.setDisplayField(name);
	}

	public void setShowFocused(Boolean showFocused) {
		impl.setShowFocused(showFocused);
	}

	public void setDefaultToFirstOption(Boolean value) {
		impl.setDefaultToFirstOption(value);
	}

}
