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
import com.nabla.wapp.client.model.field.ComboBoxField;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class ComboBox extends UiBinderFormTextItemSpeudoWidget<ComboBoxItem> {

	private static final Logger	logger = LoggerFactory.getLog(ComboBox.class);

	public ComboBox() {
		// TODO: until SmartGWT bug is resolved
		super(new ComboBoxItem() {
			@Override
			public native ListGridRecord getSelectedRecord() /*-{
    	    var self = this.@com.smartgwt.client.core.DataClass::getJsObj()();
    	    var ret = self.pickList.getSelectedRecord();
    	    if(ret == null || ret === undefined) return null;
    	    var retVal = @com.smartgwt.client.core.RefDataClass::getRef(Lcom/google/gwt/core/client/JavaScriptObject;)(ret);
    	    if(retVal == null) {
    	        retVal = @com.smartgwt.client.widgets.grid.ListGridRecord::new(Lcom/google/gwt/core/client/JavaScriptObject;)(ret);
    	    }
    	    return retVal;
    	}-*/;
		}, true);
	}

	@Override
	public FormItem getItem(final Model model) {
		if (model != null) {
			final DataSourceField field = model.getField(impl.getName());
			if (field != null && field instanceof ComboBoxField) {
				logger.warning("setting model for pick list of '" + impl.getName() + "'");
				final ComboBoxField cbField = (ComboBoxField)field;
				impl.setOptionDataSource(cbField.getModel());
				impl.setValueField(cbField.getValueField());
				impl.setDisplayField(cbField.getDisplayField());
		/*		impl.setPickListFilterCriteriaFunction(new FormItemCriteriaFunction() {

					@Override
					public Criteria getCriteria(@SuppressWarnings("unused") FormItemFunctionContext ctx) {
						final Criteria ret = cbField.getCriteria();
						final String value = impl.getDisplayValue();
						if (value != null && !value.isEmpty()) {
							final Criterion filter = new Criterion(cbField.getDisplayField(), OperatorId.ISTARTS_WITH, value);
							ret.setAttribute(IXmlRequestProtocol.XML_CRITERIA, filter.getJsObj());
						}
						return ret;
					}

				});*/
				impl.setSortField(cbField.getDisplayField());
			}
		}
		return super.getItem(model);
	}

}
