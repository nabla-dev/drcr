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
package com.nabla.wapp.client.ui;

import com.google.gwt.user.client.ui.HasText;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.model.field.SelectBoxField;
import com.nabla.wapp.client.ui.form.ControlTypes;
import com.nabla.wapp.client.ui.form.SmartGwtDateItem;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.CellFormatter;


public class ListGridField extends ListGridColumn implements HasText {

	@Override
	public String getText() {
		return impl.getTitle();
	}

	@Override
	public void setText(String text) {
		impl.setTitle(text);
	}

	public void setCanEdit(final Boolean canEdit) {
		impl.setCanEdit(canEdit);
	}

	public void setShowHover(Boolean show) {
		impl.setShowHover(show);
	}

	public void setFormatter(final CellFormatter formatter) {
		impl.setCellFormatter(formatter);
	}

	public void setIconSpacing(int spacing) {
		impl.setIconSpacing(spacing);
	}

	public void setIconWidth(int w) {
		impl.setIconWidth(w);
	}

	@Override
	com.smartgwt.client.widgets.grid.ListGridField getField(final ListGrid parent) {
		Assert.argumentNotNull(parent);

		final DataSource ds = parent.getDataSource();
		if (ds != null) {
			final DataSourceField field = ds.getField(getName());
			if (field != null) {
				if (field.getType() == FieldType.DATE) {
					impl.setEditorType(ControlTypes.createEditor(field));
					// to get calendar in pick icon instead of date range
					final SmartGwtDateItem editor = new SmartGwtDateItem();
					editor.setUseMask(true);
					impl.setFilterEditorType(editor);
				} else if (field instanceof SelectBoxField) {
					final SelectBoxField cbField = (SelectBoxField) field;
					impl.setOptionDataSource(cbField.getModel());
					impl.setValueField(cbField.getValueField());
					impl.setDisplayField(cbField.getDisplayField());
				} else if (field.getType() == FieldType.BOOLEAN) {
					impl.setCanToggle(true);
				}
			}
		}
		return super.getField(parent);
	}
}
