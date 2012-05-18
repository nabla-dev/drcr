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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.model.field.PositiveIntegerField;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * @author nabla
 *
 */
public enum ControlTypes {

	TEXT_EDIT_BOX { @Override FormItem create(@SuppressWarnings("unused") final DataSourceField field) { return new TextItem(); } },
	INTEGER_EDIT_BOX {
		@Override FormItem create(final DataSourceField field) {
			SmartGwtIntegerItem impl = new SmartGwtIntegerItem();
			if (field instanceof PositiveIntegerField)
				impl.setKeyPressFilter("^[0-9]*$");
			return impl;
		}
	},
	INTEGER_SPINNER_BOX { @Override FormItem create(@SuppressWarnings("unused") final DataSourceField field) { return new SpinnerItem(); } },
	CHECK_BOX { @Override FormItem create(@SuppressWarnings("unused") final DataSourceField field) { return new CheckboxItem(); } },
	DATE_EDIT_BOX {
		@Override FormItem create(@SuppressWarnings("unused") final DataSourceField field) {
			final DateItem impl = new SmartGwtDateItem();
			impl.setUseMask(true);
			impl.setEnforceDate(true);
			return impl;
		}
	},
	ENUM_SELECT_BOX  { @Override FormItem create(@SuppressWarnings("unused") final DataSourceField field) { return new SelectItem(); } },
	SELECT_BOX { @Override FormItem create(@SuppressWarnings("unused") final DataSourceField field) { return new SmartGwtSelectItem(); } },
/*	COMBOX_BOX {
		@Override FormItem create(final DataSourceField field) {
			final SelectItem impl = new SmartGwtSelectItem();
			impl.setPickListFilterCriteriaFunction(new FormItemCriteriaFunction() {

				@Override
				public Criteria getCriteria(@SuppressWarnings("unused") FormItemFunctionContext ctx) {
					final Criteria ret = ((ComboBoxField)field).getCriteria();
					final String value = impl.getDisplayValue();
					if (value != null && !value.isEmpty()) {
						final Criterion filter = new Criterion(impl.getDisplayField(), OperatorId.ISTARTS_WITH, value);
						ret.setAttribute(IXmlRequestProtocol.XML_CRITERIA, filter.getJsObj());
					}
					return ret;
				}
			});
			return impl;
		}
	},*/
	;

	private static final Logger	logger = LoggerFactory.getLog(ControlTypes.class);

	abstract FormItem create(final DataSourceField field);

	public static FormItem createEditor(final DataSourceField field) {
		Assert.argumentNotNull(field);

		try {
			final FormItem impl = fromClassName(field.getAttribute("editorType")).create(field);
			JSOHelper.addProperties(impl.getJsObj(), field.getAttributeAsJavaScriptObject("editorProperties"));
			return impl;
		} catch (Exception __) {
//			logger.warning("failed to get editor type for field '" + field.getName() + "'. assume default editor");
//			logger.warning(JSHelper.dump(field.getJsObj()));
			return valueOf(field.getType()).create(field);
		}
	}

	static ControlTypes valueOf(final FieldType type) {
		switch (type) {
			case TEXT:
				return ControlTypes.TEXT_EDIT_BOX;
			case INTEGER:
				return ControlTypes.INTEGER_EDIT_BOX;
			case ENUM:
				return ControlTypes.ENUM_SELECT_BOX;
			case DATE:
				return ControlTypes.DATE_EDIT_BOX;
			case BOOLEAN:
				return ControlTypes.CHECK_BOX;
			default:
				logger.log(Level.SEVERE,"unsupported control field type '" + type.toString() + "'");
				return null;
		}
	}

	static ControlTypes fromClassName(final String className) {
		if (className == null)
			return null;
		if (className.equals("SpinnerItem"))
			return ControlTypes.INTEGER_SPINNER_BOX;
		if (className.equals("SelectItem"))
			return ControlTypes.SELECT_BOX;
	/*	if (className.equals("ComboBoxItem"))
			return ControlTypes.COMBOX_BOX;*/
		logger.log(Level.SEVERE,"unsupported control class name '" + className + "'");
		return null;
	}
}
